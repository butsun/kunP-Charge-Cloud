
package org.dromara.kp.business.service;



import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;

import java.math.BigDecimal;

/**
 * @author baigod
 */
public interface PileProtocolService {
    /**
     * 桩登录
     */
    void pileLogin(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 充电桩心跳
     */
    void heartBeat(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 校验计费模型
     */
    void verifyPricing(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 查询计费策略
     */
    void queryPricing(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 上报电桩运行状态
     */
    void postGunRunStatus(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 上报充电进度
     */
    void postChargingProgress(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 费率下发反馈
     */
    void onSetPricingResponse(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 远程启动反馈
     *
     * @param uplinkQueueMessage
     */
    void onRemoteStartChargingResponse(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 远程停止反馈
     */
    void onRemoteStopChargingResponse(UplinkQueueMessage uplinkQueueMessage);

    /**
     * 交易记录上报
     */
    void onTransactionRecord(UplinkQueueMessage uplinkQueueMessage);


    void pileTryChargeRequest(UplinkQueueMessage uplinkQueueMessage);
    /**
     * 启动充电
     */
    void startCharge(String pileCode, String gunCode, BigDecimal limitYuan, String orderNo);

    /**
     * 设备对时
     * @param uplinkQueueMsg
     */
    void syncTimeResponse(UplinkQueueMessage uplinkQueueMsg);

    void lostEvent(UplinkQueueMessage uplinkQueueMsg);
}
