package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class LoginRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 设备号
     */
    private String pileCode;
    /**
     * 0 表示直流桩，1 表示交流桩
     */
    private int pileType;
    /**
     * 充电枪数量
     */
    private int gunsNum;
    /**
     * 0x00 SIM 卡
     * 0x01 LAN
     * 0x02 WAN
     * 0x03 其他
     */
    private int netType;
}
