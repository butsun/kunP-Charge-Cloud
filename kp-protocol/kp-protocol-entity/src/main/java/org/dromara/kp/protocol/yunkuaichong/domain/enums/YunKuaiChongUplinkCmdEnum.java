package org.dromara.kp.protocol.yunkuaichong.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:09
 **/
@RequiredArgsConstructor
@Getter
public enum YunKuaiChongUplinkCmdEnum {

    UNKNOWN(Integer.MAX_VALUE),

    /****************************************************************上电流程*************************************************************/

    /**
     * 充电桩登录认证
     */
    LOGIN(0x01),
    /**
     * 充电桩心跳包
     */
    HEARTBEAT(0x03),
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

    /****************************************************************充电流程*************************************************************/

    /**
     * 充电握手
     */
    CHARGE_HANDSHAKE(0x15),

    /**
     * 参数配置
     */
    PARAM_CONFIG(0x17),
    /**
     * 充电结束
     */
    CHARGE_END(0x19),

    /**
     * 错误报文
     */
    ERROR_TASK(0x1B),

    /**
     * 充电中BMS停止
     */
    CHARGING_BMS_STOP(0x1D),

    /**
     * 充电中充电机停止
     */
    CHARGING_TERMINA_STOP(0x21),

    /**
     * 充电中BMS输入输出
     */
    CHARGING_BMS_IO(0x23),

    /**
     * 充电过程 BMS 信息
     */
    CHARGING_BMS_DATA(0x25),

    /****************************************************************刷卡充电流程*************************************************************/


    /**
     * 充电桩主动申请启动充电
     */
    PILE_TRY_CHARGE(0x31),
    /**
     * 远程启动结果
     */
    REMOTE_START_RESULT(0x33),
    /**
     * 远程停机结果
     */
    REMOTE_STOP_RESULT(0x35),

    /**
     * 交易记录
     */
    TRANSACTION_RECORD(0x3B),
    /**
     * 设置计费模型应答
     */
    SET_PRICE_ACK(0x57),
    ;

    private final Integer cmd;

}
