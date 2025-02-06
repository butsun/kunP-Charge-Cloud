package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class RemoteStartChargingRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String pileCode;
    private String gunCode;
    private String tradeNo;
    private String limitYuan;
    private String additionalInfo;
}
