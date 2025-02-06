package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class RemoteStopChargingRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String pileCode;
    private String gunCode;
}
