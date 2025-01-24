
package org.dromara.kp.protocol.yunkuaichong.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author but
 */
@AllArgsConstructor
@Getter
public enum YunKuaiChongDownlinkCmdEnum{

    UNKNOWN(Integer.MAX_VALUE),

    /**
     * 登录认证应答
     */
    LOGIN_ACK(0x02),
    /**
     * 对时设置
     */
    SYNC_TIME(0x56),
    /**
     * 心跳包应答
     */
    HEARTBEAT_ACK(0x04),
    /**
     * 计费模型验证请求应答
     */
    VERIFY_PRICING_ACK(0x06),
    /**
     * 计费模型请求应答
     */
    QUERY_PRICING_ACK(0X0A),
    /**
     * 充电桩主动申请启动充电应答
     */
    PILE_TRY_CHARGE_ACK(0x32),
    /**
     * 运营平台远程控制启机
     */
    REMOTE_START_CHARGING(0x34),
    /**
     * 运营平台远程停机
     */
    REMOTE_STOP_CHARGING(0x36),
    /**
     * 交易记录确认
     */
    TRANSACTION_RECORD_ACK(0x40),
    /**
     * 计费模型设置
     */
    SET_PRICING(0x58),
    /**
     *
     */
    REMOTE_PARALLEL_START_CHARGING(0xA4);

    private final Integer cmd;

}
