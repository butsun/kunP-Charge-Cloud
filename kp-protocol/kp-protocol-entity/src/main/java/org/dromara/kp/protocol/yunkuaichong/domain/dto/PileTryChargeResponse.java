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
 * @create: 10:54
 **/

@AllArgsConstructor
@Data
@Builder
public class PileTryChargeResponse  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 16位交易流水号
     */
    private String tradeNo;

    /**
     * 设备号
     */
    private String pileCode;

    /**
     * 枪号
     */
    private String gunNo;

    /**
     * 刷卡启动时的凭证号
     */
    private long cardNo;

    /**
     * 鉴权是否成功
     */
    private boolean success;

    /**
     * 鉴权失败原因
     */
    private int failReason;



}
