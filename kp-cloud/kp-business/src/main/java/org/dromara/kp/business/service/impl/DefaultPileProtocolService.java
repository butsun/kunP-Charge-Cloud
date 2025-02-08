
package org.dromara.kp.business.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.kp.business.api.DownlinkService;
import org.dromara.kp.business.api.PileChargeService;
import org.dromara.kp.business.api.PileLeftCycleService;
import org.dromara.kp.business.service.PileProtocolService;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.*;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum;
import org.dromara.kp.protocol.yunkuaichong.domain.model.PricingModel;
import org.dromara.kp.protocol.yunkuaichong.domain.model.ProtoConverter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.*;


/**
 * @author baigod
 */
@Service
@Slf4j
public class DefaultPileProtocolService implements PileProtocolService {

    @DubboReference
    DownlinkService downlinkCallService;

    @DubboReference
    PileChargeService pileChargeClient;

    @DubboReference
    PileLeftCycleService pileLeftCycleClient;


    @Override
    public void pileLogin(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到桩登录事件 {}", uplinkQueueMessage.getLoginRequest());
        LoginRequest loginRequest = uplinkQueueMessage.getLoginRequest();

        //查找设备是否存在
        String pileCode = loginRequest.getPileCode();
        int netType = loginRequest.getNetType();
        LoginResponse loginResponse  =  pileLeftCycleClient.authPileLogin(pileCode,netType);

        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, loginRequest.getPileCode());

        downlinkMessageBuilder.downlinkCmd(YunKuaiChongDownlinkCmdEnum.LOGIN_ACK.name());
        downlinkMessageBuilder.loginResponse(loginResponse);

        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());
    }

    @Override
    public void heartBeat(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到枪心跳事件 {}", uplinkQueueMessage.getHeartBeatRequest());
//        pileLeftCycleClient.refreshPileStatus(uplinkQueueMessage.getHeartBeatRequest());
    }


    @Override
    public void verifyPricing(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到计费模型验证请求 {}", uplinkQueueMessage.getVerifyPricingRequest());

        VerifyPricingRequest verifyPricingRequest = uplinkQueueMessage.getVerifyPricingRequest();
        String pileCode = verifyPricingRequest.getPileCode();
        long pricingId = verifyPricingRequest.getPricingId();

        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, pileCode);
        downlinkMessageBuilder.downlinkCmd(YunKuaiChongDownlinkCmdEnum.VERIFY_PRICING_ACK.name());
        downlinkMessageBuilder.verifyPricingResponse(VerifyPricingResponse.builder()
            .success(false)
            .pricingId(pricingId)
            .build());
        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());
    }

    @Override
    public void queryPricing(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩计费模型请求 {}", uplinkQueueMessage.getQueryPricingRequest());

        QueryPricingRequest queryPricingRequest = uplinkQueueMessage.getQueryPricingRequest();
        String pileCode = queryPricingRequest.getPileCode();
        PricingModel model = pileChargeClient.getPilePricingModel(pileCode);

        // 构造下行计费
        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, pileCode);
        downlinkMessageBuilder.downlinkCmd(QUERY_PRICING_ACK.name());
        downlinkMessageBuilder.queryPricingResponse(QueryPricingResponse.builder()
            .pileCode(pileCode)
            .pricingId(1)
            .pricingModel(ProtoConverter.toPricingModel(model))
            .build());

        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());


    }

    @Override
    public void postGunRunStatus(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩上报的电桩状态 {}", uplinkQueueMessage.getGunRunStatusProto());
        pileLeftCycleClient.refreshGunStatus(uplinkQueueMessage.getGunRunStatusProto());
    }

    @Override
    public void postChargingProgress(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩上报的充电进度 {}", uplinkQueueMessage.getChargingProgressProto());
        // TODO 处理相关业务逻辑  找到订单计费
        pileChargeClient.refreshChargeOrder(uplinkQueueMessage.getChargingProgressProto());
    }

    @Override
    public void onSetPricingResponse(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩费率下发反馈 {}", uplinkQueueMessage.getSetPricingResponse());

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
        TransactionRecordAck transactionRecordAck =  pileChargeClient.pileChargeTransactionRecord(uplinkQueueMessage.getTransactionRecord());

        // 构造下行计费
        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, uplinkQueueMessage.getTransactionRecord().getPileCode());

        downlinkMessageBuilder.downlinkCmd(TRANSACTION_RECORD_ACK.name());
        downlinkMessageBuilder.transactionRecordAck(transactionRecordAck);
        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());

    }

    @Override
    public void pileTryChargeRequest(UplinkQueueMessage uplinkQueueMessage) {
        log.info("接收到充电桩主动发起充电 {}", uplinkQueueMessage.getPileTryChargeRequest());
        PileTryChargeRequest pileTryChargeRequest = uplinkQueueMessage.getPileTryChargeRequest();

        PileTryChargeResponse response = pileChargeClient.tryCharge(pileTryChargeRequest);
        DownlinkRequestMessage.DownlinkRequestMessageBuilder downlinkMessageBuilder = createDownlinkMessageBuilder(uplinkQueueMessage, pileTryChargeRequest.getPileCode());
        downlinkMessageBuilder.downlinkCmd(PILE_TRY_CHARGE_ACK.name());
        downlinkMessageBuilder.pileTryChargeResponse(response);
        downlinkCallService.downlinkCmdProcess(downlinkMessageBuilder.build());

    }

    @Override
    public void syncTimeResponse(UplinkQueueMessage uplinkQueueMsg) {
        log.info("接收到充电桩对时响应 {}", uplinkQueueMsg.getSyncTimeResponse());
        SyncTimeResponse syncTimeResponse = uplinkQueueMsg.getSyncTimeResponse();
        pileLeftCycleClient.syncTime(syncTimeResponse.getPileCode(), DateUtil.date(syncTimeResponse.getCurrentTime().toEpochMilli()));
    }


    @Override
    public void lostEvent(UplinkQueueMessage uplinkQueueMsg) {
        log.info("接收到充电桩失去链接 {}", uplinkQueueMsg.getPileLostEvent());
        pileLeftCycleClient.lost(uplinkQueueMsg.getPileLostEvent());
    }

    //远程下发启动响应
    @Override
    public void startCharge(String pileCode, String gunCode, BigDecimal limitYuan, String orderNo) {

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
