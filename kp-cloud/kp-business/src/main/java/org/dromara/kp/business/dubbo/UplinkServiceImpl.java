package org.dromara.kp.business.dubbo;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.kp.business.api.UplinkService;
import org.dromara.kp.business.service.PileProtocolService;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;

import java.util.Objects;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 17:33
 **/
@DubboService
@Slf4j
public class UplinkServiceImpl implements UplinkService {

    @Resource
    private PileProtocolService pileProtocolService;

    @Override
    public void uplinkCmdProcess(UplinkQueueMessage uplinkQueueMsg) {
        try {
            if (hasMessage(uplinkQueueMsg.getLoginRequest())) {

                pileProtocolService.pileLogin(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getHeartBeatRequest())) {

                pileProtocolService.heartBeat(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getVerifyPricingRequest())) {

                pileProtocolService.verifyPricing(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getQueryPricingRequest())) {

                pileProtocolService.queryPricing(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getGunRunStatusProto())) {

                pileProtocolService.postGunRunStatus(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getChargingProgressProto())) {

                pileProtocolService.postChargingProgress(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getSetPricingResponse())) {

                pileProtocolService.onSetPricingResponse(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getRemoteStartChargingResponse())) {

                pileProtocolService.onRemoteStartChargingResponse(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getRemoteStopChargingResponse())) {

                pileProtocolService.onRemoteStopChargingResponse(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getTransactionRecord())) {

                pileProtocolService.onTransactionRecord(uplinkQueueMsg);

            } else if (hasMessage(uplinkQueueMsg.getPileTryChargeRequest())){

                pileProtocolService.pileTryChargeRequest(uplinkQueueMsg);
            } else if (hasMessage(uplinkQueueMsg.getSyncTimeResponse())){
                pileProtocolService.syncTimeResponse(uplinkQueueMsg);
            } else if (hasMessage(uplinkQueueMsg.getPileLostEvent())){
                pileProtocolService.lostEvent(uplinkQueueMsg);
            }
            else {
                log.warn("uplinkMsg未找到可用实现, {}", uplinkQueueMsg);
            }

        } catch (Exception e) {
            log.warn("uplinkMsg处理失败, {}", uplinkQueueMsg);
            throw e;
        }
    }

    private boolean hasMessage(Object msg) {
        return !Objects.equals(null, msg);
    }
}
