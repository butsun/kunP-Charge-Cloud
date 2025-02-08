package org.dromara.kp.system.dubbo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.kp.business.api.PileChargeService;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.*;
import org.dromara.kp.protocol.yunkuaichong.domain.model.PricingModel;
import org.dromara.kp.system.domain.KpChargeOrder;
import org.dromara.kp.system.domain.KpConnector;
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.service.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto.PricingModelFlag.*;
import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto.PricingModelFlag.VALLEY;
import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PricingModelProto.PricingModelRule.SPLIT_TIME;
import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PricingModelProto.PricingModelType.CHARGE;

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
    private final IKpStationService stationService;
    private final IKpOperatorService operatorService;
    private final IKpChargeOrderService chargeOrderService;


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

        //todo  还有卡校验等操作
        verifyChargeSession();
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
        chargeOrder.setSoc(new BigDecimal(chargingProgressProto.getSoc()));
        chargeOrder.setEndTime(DateUtil.date());
        chargeOrder.setTotalPower(new BigDecimal(chargingProgressProto.getTotalChargingEnergyKWh()));
        chargeOrder.setGunCurrent(new BigDecimal(chargingProgressProto.getOutputCurrent()));
        chargeOrder.setGunVoltage(new BigDecimal(chargingProgressProto.getOutputVoltage()));
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
        //Todo  补充订单详情
        String tradeNo = transactionRecord.getTradeNo();
        String pileCode = transactionRecord.getPileCode();
        int gunCode = transactionRecord.getGunCode();
        KpChargeOrder chargeOrder = chargeOrderService.queryByTradeNo(tradeNo, pileCode, gunCode);

        if (flag = Objects.nonNull(chargeOrder)) {
            chargeOrder.setStartChargeSeqStat(4);
            chargeOrder.setEndTime(DateUtil.date());
            chargeOrder.setTotalPower(new BigDecimal(transactionRecord.getTotalEnergyKWh()));
            chargeOrder.setStopReason(transactionRecord.getStopReason());
        }

        flag = chargeOrderService.refreshOrder(chargeOrder);

        return TransactionRecordAck.builder()
            .tradeNo(tradeNo)
            .success(flag)
            .build();
    }


    @Override
    public PricingModel getPilePricingModel(String pileCode) {

        // TODO 先构造一个通用的计费模型，后续根据业务做库查询
        List<PricingModel.Period> periods = new ArrayList<>();

        periods.add(createPeriod(1, LocalTime.parse("00:00"), LocalTime.parse("06:00"), TOP));
        periods.add(createPeriod(2, LocalTime.parse("06:00"), LocalTime.parse("12:00"), PEAK));
        periods.add(createPeriod(3, LocalTime.parse("12:00"), LocalTime.parse("18:00"), FLAT));
        periods.add(createPeriod(4, LocalTime.parse("18:00"), LocalTime.parse("00:00"), VALLEY));

        Map<PeriodProto.PricingModelFlag, PricingModel.FlagPrice> flagPriceMap = new HashMap<>();
        flagPriceMap.put(TOP, new PricingModel.FlagPrice(new BigDecimal("1.00"), new BigDecimal("1.00")));
        flagPriceMap.put(PEAK, new PricingModel.FlagPrice(new BigDecimal("1.00"), new BigDecimal("1.00")));
        flagPriceMap.put(FLAT, new PricingModel.FlagPrice(new BigDecimal("1.00"), new BigDecimal("1.00")));
        flagPriceMap.put(VALLEY, new PricingModel.FlagPrice(new BigDecimal("1.00"), new BigDecimal("1.00")));

        PricingModel model = new PricingModel();
        model.setId(UUID.randomUUID());
        model.setSequenceNumber(1);
        model.setPileCode(pileCode);
        model.setType(CHARGE);
        model.setRule(SPLIT_TIME);
        model.setStandardElec(new BigDecimal("1.00"));
        model.setStandardServ(new BigDecimal("1.00"));
        model.setFlagPriceList(flagPriceMap);
        model.setPeriodsList(periods);

        return model;
    }

    private static PricingModel.Period createPeriod(int sn, LocalTime beginTime, LocalTime endTime, PeriodProto.PricingModelFlag flag) {
        PricingModel.Period period = new PricingModel.Period();
        period.setSn(sn);
        period.setBegin(beginTime);
        period.setEnd(endTime);
        period.setFlag(flag);
        return period;
    }
}
