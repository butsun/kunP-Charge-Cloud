
package org.dromara.kp.business.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.kp.business.api.DownlinkService;
import org.dromara.kp.business.service.PileProtocolService;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.*;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum;
import org.dromara.kp.protocol.yunkuaichong.domain.model.PricingModel;
import org.dromara.kp.protocol.yunkuaichong.domain.model.ProtoConverter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto.PricingModelFlag.*;
import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PricingModelProto.PricingModelRule.SPLIT_TIME;
import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PricingModelProto.PricingModelType.CHARGE;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.*;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum.TRANSACTION_RECORD;


/**
 * @author baigod
 */
@Service
@Slf4j
public class DefaultPileProtocolService implements PileProtocolService {

    @DubboReference
    DownlinkService downlinkCallService;

    @Override
    public void pileLogin(UplinkQueueMessage uplinkQueueMessage) {
        log.debug("接收到桩登录事件 {}", uplinkQueueMessage.getLoginRequest());
        LoginRequest loginRequest = uplinkQueueMessage.getLoginRequest();

        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, loginRequest.getPileCode());

        downlinkMessageBuilder.downlinkCmd(YunKuaiChongDownlinkCmdEnum.LOGIN_ACK.name());
        LoginResponse loginResponse = LoginResponse.builder()
            .pileCode(loginRequest.getPileCode())
            .success(true)
            .build();
        downlinkMessageBuilder.loginResponse(loginResponse);

        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());
    }

    @Override
    public void heartBeat(UplinkQueueMessage uplinkQueueMessage) {
        log.debug("接收到桩心跳事件 {}", uplinkQueueMessage);
    }


    @Override
    public void verifyPricing(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到计费模型验证请求 {}", uplinkQueueMessage.getVerifyPricingRequest());

        VerifyPricingRequest verifyPricingRequest = uplinkQueueMessage.getVerifyPricingRequest();
        String pileCode = verifyPricingRequest.getPileCode();

        long pricingId = verifyPricingRequest.getPricingId();
        // todo 默认校验成功，后续查库校验
        assert pricingId > 0;

        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, pileCode);
        downlinkMessageBuilder.downlinkCmd(YunKuaiChongDownlinkCmdEnum.VERIFY_PRICING_ACK.name());
        downlinkMessageBuilder.verifyPricingResponse(VerifyPricingResponse.builder()
            .success(true)
            .pricingId(pricingId)
            .build());
        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());
    }

    @Override
    public void queryPricing(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩计费模型请求 {}", uplinkQueueMessage.getQueryPricingRequest());

        QueryPricingRequest queryPricingRequest = uplinkQueueMessage.getQueryPricingRequest();
        String pileCode = queryPricingRequest.getPileCode();

        // TODO 先构造一个通用的计费模型，后续根据业务做库查询
        List<PricingModel.Period> periods = new ArrayList<>();

        periods.add(createPeriod(1, LocalTime.parse("00:00"), LocalTime.parse("06:00"), TOP));
        periods.add(createPeriod(2, LocalTime.parse("06:00"), LocalTime.parse("12:00"), PEAK));
        periods.add(createPeriod(3, LocalTime.parse("12:00"), LocalTime.parse("18:00"), FLAT));
        periods.add(createPeriod(4, LocalTime.parse("18:00"), LocalTime.parse("00:00"), VALLEY));

        Map<PeriodProto.PricingModelFlag, PricingModel.FlagPrice> flagPriceMap = new HashMap<>();
        flagPriceMap.put(TOP, new PricingModel.FlagPrice(new BigDecimal("3.00"), new BigDecimal("1.00")));
        flagPriceMap.put(PEAK, new PricingModel.FlagPrice(new BigDecimal("3.00"), new BigDecimal("1.00")));
        flagPriceMap.put(FLAT, new PricingModel.FlagPrice(new BigDecimal("3.00"), new BigDecimal("1.00")));
        flagPriceMap.put(VALLEY, new PricingModel.FlagPrice(new BigDecimal("3.00"), new BigDecimal("1.00")));

        PricingModel model = new PricingModel();
        model.setId(UUID.randomUUID());
        model.setSequenceNumber(1);
        model.setPileCode(pileCode);
        model.setType(CHARGE);
        model.setRule(SPLIT_TIME);
        model.setStandardElec(new BigDecimal("3.00"));
        model.setStandardServ(new BigDecimal("1.00"));
        model.setFlagPriceList(flagPriceMap);
        model.setPeriodsList(periods);

        // 构造下行计费
        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, pileCode);
        downlinkMessageBuilder.downlinkCmd(QUERY_PRICING_ACK.name());
        downlinkMessageBuilder.queryPricingResponse(QueryPricingResponse.builder()
            .pileCode(pileCode)
            .pricingId(model.getSequenceNumber())
            .pricingModel(ProtoConverter.toPricingModel(model))
            .build());

        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());


    }

    @Override
    public void postGunRunStatus(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩上报的电桩状态 {}", uplinkQueueMessage.getGunRunStatusProto());

        // TODO 处理相关业务逻辑


    }

    @Override
    public void postChargingProgress(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩上报的充电进度 {}", uplinkQueueMessage.getChargingProgressProto());

        // TODO 处理相关业务逻辑


    }

    @Override
    public void onSetPricingResponse(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩上费率下发反馈 {}", uplinkQueueMessage.getSetPricingResponse());

        // TODO 处理相关业务逻辑


    }

    @Override
    public void onRemoteStartChargingResponse(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩启动结果反馈 {}", uplinkQueueMessage.getRemoteStartChargingResponse());

        // TODO 处理相关业务逻辑


    }

    @Override
    public void onRemoteStopChargingResponse(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩停止结果反馈 {}", uplinkQueueMessage.getRemoteStopChargingResponse());

        // TODO 处理相关业务逻辑


    }

    @Override
    public void onTransactionRecord(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩交易记录上报 {}", uplinkQueueMessage.getTransactionRecord());

        // todo 毛都不敢先给个回复
        TransactionRecord transactionRecord = uplinkQueueMessage.getTransactionRecord();

        String tradeNo = transactionRecord.getTradeNo();
        String pileCode = transactionRecord.getPileCode();

        // 构造下行计费
        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, pileCode);
        downlinkMessageBuilder.downlinkCmd(TRANSACTION_RECORD_ACK.name());
        downlinkMessageBuilder.transactionRecordAck(TransactionRecordAck.builder()
            .tradeNo(tradeNo)
            .success(true)
            .build());
        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());

    }

    @Override
    public void pileTryChargeRequest(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩主动发起充电 {}", uplinkQueueMessage.getPileTryChargeRequest());
        PileTryChargeRequest pileTryChargeRequest = uplinkQueueMessage.getPileTryChargeRequest();
        String pileCode = pileTryChargeRequest.getPileCode();
        String gunNo = pileTryChargeRequest.getGunNo();

        // todo 毛都不敢先给个回复

        //        32010600019236 01 20010618034230 60。
        // 格式桩号（7bytes）+枪号（1byte）+年月日时分秒（6bytes）200106180342 +自增序号（2bytes）
        String tradeNo = pileCode + gunNo + DateUtil.format(new Date(), "yyyyMMddHHmmss") + "01" ;
        // 构造下行计费
        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, pileCode);
        downlinkMessageBuilder.downlinkCmd(PILE_TRY_CHARGE_ACK.name());
        downlinkMessageBuilder.pileTryChargeResponse(PileTryChargeResponse.builder()
            .tradeNo(tradeNo)
            .gunNo(gunNo)
            .cardNo(pileTryChargeRequest.getCardNo())
            .failReason(0)
            .pileCode(pileCode)
            .success(true)
            .build());
        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());

    }

    //远程下发启动响应
    @Override
    public void startCharge(String pileCode, String gunCode, BigDecimal limitYuan, String orderNo) {

    }

    private static PricingModel.Period createPeriod(int sn, LocalTime beginTime, LocalTime endTime, PeriodProto.PricingModelFlag flag) {
        PricingModel.Period period = new PricingModel.Period();
        period.setSn(sn);
        period.setBegin(beginTime);
        period.setEnd(endTime);
        period.setFlag(flag);
        return period;
    }

    private DownlinkRequestMessage.DownlinkRequestMessageBuilder createDownlinkMessageBuilder(UplinkQueueMessage uplinkQueueMessage, String pileCode) {
        UUID messageId = UUID.randomUUID();
        DownlinkRequestMessage.DownlinkRequestMessageBuilder builder = DownlinkRequestMessage.builder();
        builder.messageIdMSB(messageId.getLeastSignificantBits());
        builder.messageIdLSB(messageId.getLeastSignificantBits());
        builder.pileCode(pileCode);
        builder.sessionIdMSB(uplinkQueueMessage.getSessionIdMSB());
        builder.sessionIdLSB(uplinkQueueMessage.getSessionIdLSB());
        builder.protocolName(uplinkQueueMessage.getProtocolName());
        builder.requestIdMSB(uplinkQueueMessage.getMessageIdMSB());
        builder.requestIdLSB(uplinkQueueMessage.getMessageIdLSB());
        builder.requestData(uplinkQueueMessage.getRequestData());
        return builder;
    }
}
