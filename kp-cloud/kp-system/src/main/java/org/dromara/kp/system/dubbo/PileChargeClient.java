package org.dromara.kp.system.dubbo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.kp.business.api.PileChargeService;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.*;
import org.dromara.kp.protocol.yunkuaichong.domain.model.PricingModel;
import org.dromara.kp.system.domain.*;
import org.dromara.kp.system.domain.vo.KpChargeAccountVo;
import org.dromara.kp.system.service.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto.PricingModelFlag.*;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:07
 **/
@DubboService
@Slf4j
@RequiredArgsConstructor
public class PileChargeClient implements PileChargeService {

    private final IKpEquipmentService equipmentService;
    private final IKpConnectorService connectorService;
    private final IKpChargeOrderService chargeOrderService;
    private final IKpChargeVoucherService chargeVoucherService;
    private final IKpPriceTemplateService priceTemplateService;
    private final IKpDiscountActivityService discountActivityService;
    private final IKpChargeAccountService chargeAccountService;
    private final IKpUserCarService userCarService;


    @Override
    public PileTryChargeResponse tryCharge(PileTryChargeRequest pileTryChargeRequest) {
        String pileCode = pileTryChargeRequest.getPileCode();
        String gunNo = pileTryChargeRequest.getGunNo();
        long cardNo = pileTryChargeRequest.getCardNo();
        int chargeType = pileTryChargeRequest.getChargeType();
        String carVin = pileTryChargeRequest.getCarVin();
        Date startDate = DateUtil.date();

        KpEquipment equipment = equipmentService.queryByEquipmentNo(pileCode);
        KpConnector connector = connectorService.queryByNo(pileCode, Integer.parseInt(gunNo));

        //todo  还有卡校验等操作  凭证是否存在
        String tradeNo = generateChargeTradeNo(pileCode, gunNo, startDate);
        String startChargeSeq = "OKP" + tradeNo;
        KpChargeOrder kpChargeOrder = new KpChargeOrder();
        kpChargeOrder.setStartChargeSeq(startChargeSeq);
        kpChargeOrder.setTradeNo(tradeNo);
        kpChargeOrder.setStartChargeSeqStat(1);
        kpChargeOrder.setStationId(equipment.getStationId());
        kpChargeOrder.setConnectorId(connector.getId());
        kpChargeOrder.setEquipmentId(equipment.getId());
        kpChargeOrder.setOperatorId(equipment.getOperatorId());
        kpChargeOrder.setStartTime(startDate);
        kpChargeOrder.setCarVin(carVin);
        kpChargeOrder.setStartType(chargeType);
        kpChargeOrder.setVoucherNo(chargeType == 1 ? cardNo + "" : carVin);
        kpChargeOrder.setConnectorNo(connector.getConnectorNo());
        kpChargeOrder.setEquipmentNo(equipment.getEquipmentNo());

        //通过凭证获取账户id
        KpChargeVoucher kpChargeVoucher = chargeVoucherService.queryByVoucherVoNo(kpChargeOrder.getVoucherNo());
        if (Objects.isNull(kpChargeVoucher)) {
            return PileTryChargeResponse.builder()
                .tradeNo(tradeNo)
                .gunNo(gunNo)
                .cardNo(pileTryChargeRequest.getCardNo())
                .failReason(0)
                .pileCode(pileCode)
                .success(false)
                .build();
        }


        kpChargeOrder.setAccountId(kpChargeVoucher.getAccountId());
        KpChargeAccountVo kpChargeAccountVo = chargeAccountService.queryById(kpChargeOrder.getAccountId());

        //通过运营商获取折扣Id
        KpDiscountActivity kpDiscountActivity = discountActivityService.queryByOperatorId(equipment.getOperatorId(), kpChargeAccountVo.getAccoutType());
        if (Objects.nonNull(kpDiscountActivity)) {
            kpChargeOrder.setAccountId(kpDiscountActivity.getId());
            kpChargeOrder.setActivittyElec(kpDiscountActivity.getDisElectricity());
            kpChargeOrder.setActivityService(kpDiscountActivity.getDisService());
        }

        //通过站点获取 下单时计价快照
        KpPriceTemplate kpPriceTemplate = priceTemplateService.queryByStationId(equipment.getStationId());
        PricingModel pricingModel = buildPricingModel(kpPriceTemplate);
        kpChargeOrder.setPriceInfo(JSONUtil.toJsonStr(pricingModel));

        //通过VIN获取车牌号
        KpUserCar kpUserCar = userCarService.queryByVin(carVin);
        if (Objects.nonNull(kpUserCar)) {
            kpChargeOrder.setPlateNum(kpUserCar.getPlateNo());
        }

        boolean result = chargeOrderService.insertOrder(kpChargeOrder);
        return PileTryChargeResponse.builder()
            .tradeNo(tradeNo)
            .gunNo(gunNo)
            .cardNo(pileTryChargeRequest.getCardNo())
            .failReason(0)
            .pileCode(pileCode)
            .success(result)
            .build();
    }

    @Override
    public void refreshChargeOrder(ChargingProgressProto chargingProgressProto) {
        String pileCode = chargingProgressProto.getPileCode();
        int gunCode = chargingProgressProto.getGunCode();
        String tradeNo = chargingProgressProto.getTradeNo();
        KpChargeOrder chargeOrder = chargeOrderService.queryByTradeNo(tradeNo, pileCode, gunCode);
        if (Objects.isNull(chargeOrder)) {
            //找不到订单直接返回
            return;
        }
        chargeOrder.setStartChargeSeqStat(2);
        chargeOrder.setSoc(new BigDecimal(chargingProgressProto.getSoc()));
        chargeOrder.setEndTime(DateUtil.date());
        chargeOrder.setGunCurrent(new BigDecimal(chargingProgressProto.getOutputCurrent()));
        chargeOrder.setGunVoltage(new BigDecimal(chargingProgressProto.getOutputVoltage()));
        chargeOrder.setUpdateTime(DateUtil.date());
        calculateChargePrice(chargeOrder, new BigDecimal(chargingProgressProto.getTotalChargingEnergyKWh()), chargingProgressProto.getTs());

        chargeOrderService.refreshOrder(chargeOrder);
    }

    private String generateChargeTradeNo(String pileCode, String gunNo, Date startDate) {
        return pileCode + gunNo + DateUtil.format(startDate, "yyyyMMddHHmmss") + "01";
    }

    private boolean verifyChargeSession() {
        return true;
    }


    @Override
    public TransactionRecordAck pileChargeTransactionRecord(TransactionRecord transactionRecord) {
        boolean flag;
        String tradeNo = transactionRecord.getTradeNo();
        String pileCode = transactionRecord.getPileCode();
        int gunCode = transactionRecord.getGunCode();
        KpChargeOrder chargeOrder = chargeOrderService.queryByTradeNo(tradeNo, pileCode, gunCode);

        if (flag = Objects.nonNull(chargeOrder)) {
            chargeOrder.setStartChargeSeqStat(4);
            chargeOrder.setEndTime(DateUtil.date());
            chargeOrder.setStopReason(transactionRecord.getStopReason());
            chargeOrder.setUpdateTime(DateUtil.date());
            chargeOrder = calculateChargePrice(chargeOrder, new BigDecimal(transactionRecord.getTotalEnergyKWh()), transactionRecord.getEndTs());
            flag = chargeOrderService.refreshOrder(chargeOrder);
        }
        return TransactionRecordAck.builder()
            .tradeNo(tradeNo)
            .success(flag)
            .build();
    }

    private KpChargeOrder calculateChargePrice(KpChargeOrder chargeOrder, BigDecimal currentPower, long currentTs) {
        BigDecimal totalPower = chargeOrder.getTotalPower();
        BigDecimal topPower = chargeOrder.getTopPower();
        BigDecimal flatPower = chargeOrder.getFlatPower();
        BigDecimal valleyPower = chargeOrder.getValleyPower();
        BigDecimal peakPower = chargeOrder.getPeakPower();

        //计算区间电量
        BigDecimal intervalPower = NumberUtil.sub(currentPower, totalPower);

        //找出当前时间段所处的类型
        PricingModel pilePricingModel = JSONUtil.toBean(chargeOrder.getPriceInfo(), PricingModel.class);

        List<PricingModel.Period> periods = pilePricingModel.getPeriodsList();
        // 将当前时间戳转换为LocalTime
        LocalTime currentTime = LocalTime.ofInstant(Instant.ofEpochMilli(currentTs), ZoneId.systemDefault());
        PeriodProto.PricingModelFlag flag = periods.stream()
            .filter(period -> {
                LocalTime beginTime = period.getBegin();
                LocalTime endTime = "00:00".equals(period.getEnd().toString()) ?
                    LocalTime.MAX : period.getEnd();
                // 判断当前时间是否在时间段内
                return (currentTime.equals(beginTime) || currentTime.isAfter(beginTime))
                    && (currentTime.isBefore(endTime) || currentTime.equals(endTime));
            })
            .findFirst()
            .map(PricingModel.Period::getFlag)
            .orElse(FLAT);// 如果没找到匹配的时间段，返回平段

        switch (flag) {
            case TOP:
                topPower = intervalPower.add(chargeOrder.getTopPower());
                break;
            case PEAK:
                peakPower = intervalPower.add(chargeOrder.getPeakPower());
                break;
            case VALLEY:
                valleyPower = intervalPower.add(chargeOrder.getValleyPower());
                break;
            default:
                flatPower = intervalPower.add(chargeOrder.getFlatPower());
                break;
        }


        Map<PeriodProto.PricingModelFlag, PricingModel.FlagPrice> flagPriceMap = pilePricingModel.getFlagPriceList();
        // 计算尖峰时段费用
        PricingModel.FlagPrice topPrice = flagPriceMap.get(TOP);
        BigDecimal topElectricityFee = topPower.multiply(topPrice.getElec()).setScale(4, RoundingMode.DOWN);
        BigDecimal topServiceFee = topPower.multiply(topPrice.getServ()).setScale(4, RoundingMode.DOWN);

        // 计算峰时段费用
        PricingModel.FlagPrice peakPrice = flagPriceMap.get(PEAK);
        BigDecimal peakElectricityFee = peakPower.multiply(peakPrice.getElec()).setScale(4, RoundingMode.DOWN);
        BigDecimal peakServiceFee = peakPower.multiply(peakPrice.getServ()).setScale(4, RoundingMode.DOWN);

        // 计算谷时段费用
        PricingModel.FlagPrice valleyPrice = flagPriceMap.get(VALLEY);
        BigDecimal valleyElectricityFee = valleyPower.multiply(valleyPrice.getElec()).setScale(4, RoundingMode.DOWN);
        BigDecimal valleyServiceFee = valleyPower.multiply(valleyPrice.getServ()).setScale(4, RoundingMode.DOWN);

        // 计算平时段费用
        PricingModel.FlagPrice flatPrice = flagPriceMap.get(FLAT);
        BigDecimal flatElectricityFee = flatPower.multiply(flatPrice.getElec()).setScale(4, RoundingMode.DOWN);
        BigDecimal flatServiceFee = flatPower.multiply(flatPrice.getServ()).setScale(4, RoundingMode.DOWN);

        // 设置总电量和分时段电量
        chargeOrder.setTotalPower(currentPower);
        chargeOrder.setTopPower(topPower);
        chargeOrder.setFlatPower(flatPower);
        chargeOrder.setValleyPower(valleyPower);
        chargeOrder.setPeakPower(peakPower);


        // 计算总费用
        BigDecimal totalElectricityFee = topElectricityFee.add(peakElectricityFee).add(valleyElectricityFee).add(flatElectricityFee);
        BigDecimal totalServiceFee = topServiceFee.add(peakServiceFee).add(valleyServiceFee).add(flatServiceFee);
        chargeOrder.setElecMoney(totalElectricityFee);
        chargeOrder.setServiceMoney(totalServiceFee);
        chargeOrder.setTotalMoney(totalElectricityFee.add(totalServiceFee));


        //计算优惠费用
        BigDecimal finalElecMoney = totalElectricityFee.multiply(chargeOrder.getActivittyElec()).setScale(4, RoundingMode.DOWN);
        BigDecimal finalServiceMoney = totalServiceFee.multiply(chargeOrder.getActivityService()).setScale(4, RoundingMode.DOWN);
        chargeOrder.setFinalElecMoney(finalElecMoney);
        chargeOrder.setFinalServiceMoney(finalServiceMoney);
        chargeOrder.setFinalTotalMoney(finalElecMoney.add(finalServiceMoney));
        return chargeOrder;
    }

    @Override
    public PricingModel getPilePricingModel(String pileCode) {
        KpEquipment equipment = equipmentService.queryByEquipmentNo(pileCode);
        //通过站点获取 下单时计价快照
        KpPriceTemplate kpPriceTemplate = priceTemplateService.queryByStationId(equipment.getStationId());
        return buildPricingModel(kpPriceTemplate);
    }

    private static PricingModel.Period createPeriod(int sn, LocalTime beginTime, LocalTime endTime, PeriodProto.PricingModelFlag flag) {
        PricingModel.Period period = new PricingModel.Period();
        period.setSn(sn);
        period.setBegin(beginTime);
        period.setEnd(endTime);
        period.setFlag(flag);
        return period;
    }


    private PricingModel buildPricingModel(KpPriceTemplate kpPriceTemplate) {
        if (kpPriceTemplate == null) {
            return getDefaultPriceTemplate();
        }
        List<PricingModel.Period> periods = new ArrayList<>();
        JSONArray periodJsonArray = JSONArray.parseArray(kpPriceTemplate.getPeriods());

        for (int i = 0; i < periodJsonArray.size(); i++) {
            JSONObject periodJson = periodJsonArray.getJSONObject(i);
            String start = periodJson.getString("start");
            String end = periodJson.getString("end");
            int flag = periodJson.getInteger("flag");
            // 将数字标志转换为对应的PricingModelFlag
            PeriodProto.PricingModelFlag modelFlag = switch (flag) {
                case 1 -> TOP;
                case 2 -> PEAK;
                case 3 -> FLAT;
                case 4 -> VALLEY;
                default -> FLAT; // 默认为平段
            };
            periods.add(createPeriod(i + 1, LocalTime.parse(start), LocalTime.parse(end), modelFlag));
        }

        Map<PeriodProto.PricingModelFlag, PricingModel.FlagPrice> flagPriceMap = new HashMap<>();
        flagPriceMap.put(TOP, new PricingModel.FlagPrice(kpPriceTemplate.getTopElecPrice(), kpPriceTemplate.getTopServPrice()));
        flagPriceMap.put(PEAK, new PricingModel.FlagPrice(kpPriceTemplate.getPeakElecPrice(), kpPriceTemplate.getPeakServPrice()));
        flagPriceMap.put(FLAT, new PricingModel.FlagPrice(kpPriceTemplate.getFlatElecPrice(), kpPriceTemplate.getFlatServPrice()));
        flagPriceMap.put(VALLEY, new PricingModel.FlagPrice(kpPriceTemplate.getValleyElecPrice(), kpPriceTemplate.getValleyServPrice()));

        PricingModel model = new PricingModel();
        model.setFlagPriceList(flagPriceMap);
        model.setPeriodsList(periods);
        return model;
    }


    private PricingModel getDefaultPriceTemplate() {
        List<PricingModel.Period> periods = new ArrayList<>();

        periods.add(createPeriod(1, LocalTime.parse("00:00"), LocalTime.parse("12:00"), FLAT));
        periods.add(createPeriod(2, LocalTime.parse("12:00"), LocalTime.parse("00:00"), FLAT));

        Map<PeriodProto.PricingModelFlag, PricingModel.FlagPrice> flagPriceMap = new HashMap<>();
        flagPriceMap.put(TOP, new PricingModel.FlagPrice(new BigDecimal("0"), new BigDecimal("0")));
        flagPriceMap.put(PEAK, new PricingModel.FlagPrice(new BigDecimal("0"), new BigDecimal("0")));
        flagPriceMap.put(FLAT, new PricingModel.FlagPrice(new BigDecimal("0"), new BigDecimal("0")));
        flagPriceMap.put(VALLEY, new PricingModel.FlagPrice(new BigDecimal("0"), new BigDecimal("0")));

        PricingModel model = new PricingModel();
        model.setFlagPriceList(flagPriceMap);
        model.setPeriodsList(periods);
        return model;
    }

}
