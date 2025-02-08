package org.dromara.kp.business.api.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 17:57
 **/
@RequiredArgsConstructor
@Getter
public enum ConnectStatusConvertEnum {


    /**
     * 充电设备接口状态:0、离网;1、空闲;2、占用(未充电);3、占用(充电中);4、占用(预约锁定);255、故障
     */

    OFFLINE(0, "UNKNOWN"),
    IDLE(1, "IDLE"),
    INSERTED(2,"INSERTED"),
    CHARGING(3,"CHARGING"),
    FAULT(255,"FAULT");

    private final Integer code;
    private final String status;


    public static Integer getCode(String status) {
        for (ConnectStatusConvertEnum value : ConnectStatusConvertEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value.getCode();
            }
        }
        return OFFLINE.getCode();
    }

}
