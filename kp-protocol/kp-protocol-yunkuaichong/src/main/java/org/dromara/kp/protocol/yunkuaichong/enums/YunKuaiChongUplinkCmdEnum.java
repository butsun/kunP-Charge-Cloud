package org.dromara.kp.protocol.yunkuaichong.enums;

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

    LOGIN(0x01),
    HEART_BEAT(0x03),
    VERIFY_PRICING_MODEL(0x05),
    QUERY_PRICING_MODEL(0x09),
    REAL_TIME_DATA(0x13),
    CHARGE_HANDSHAKE(0x15),
    CHARGE_END(0x19),
    ERROR_TASK(0x1B),




    PILE_TRY_CHARGE(0x31),
    CHARGING_BMS_DATA(0x25),
    ;


    private final Integer cmd;

}
