package org.dromara.kp.protocol.yunkuaichong.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:09
 **/
@AllArgsConstructor
@Getter
public enum YunKuaiChongUplinkCmdEnum {

    /**
     * 充电桩登录认证
     */
    LOGIN(0x01),
    /**
     * 充电桩心跳包
     */
    HEART_BEAT(0x03),
    /**
     * 计费模型验证请求
     */
    VERIFY_PRICING_MODEL(0x05),
    /**
     * 充电桩计费模型请求
     */
    QUERY_PRICING_MODEL(0x09),
    /**
     * 离线监测数据
     */
    REAL_TIME_DATA(0x13),
    /**
     * 充电握手
     */
    CHARGE_HANDSHAKE(0x15),
    /**
     * 充电结束
     */
    CHARGE_END(0x19),
    /**
     * 充电桩登录认证
     */
    ERROR_TASK(0x1B),
    /**
     * 错误报文
     */
    PILE_TRY_CHARGE(0x31),
    /**
     * 充电过程 BMS 信息
     */
    CHARGING_BMS_DATA(0x25),
    ;

    private final Integer cmd;

}
