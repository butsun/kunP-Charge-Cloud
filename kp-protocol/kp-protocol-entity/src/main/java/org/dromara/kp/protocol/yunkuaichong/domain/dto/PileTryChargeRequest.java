package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 10:38
 **/

@AllArgsConstructor
@Data
@Builder
public class PileTryChargeRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备号
     */
    private String pileCode;

    /**
     * 枪号
     */
    private String gunNo;

    /**
     * 启动方式  01刷卡  03VIN
     */
    private int chargeType;

    /**
     * 刷卡启动时的凭证号
     */
    private long cardNo;

    /**
     * VIN启动时的凭证号
     */
    private String carVin;

}
