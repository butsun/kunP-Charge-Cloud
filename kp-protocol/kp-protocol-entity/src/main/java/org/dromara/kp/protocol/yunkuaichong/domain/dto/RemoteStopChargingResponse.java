package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RemoteStopChargingResponse {
    private long ts;
    private String pileCode;
    private String gunCode;
    private boolean success;
    private String failReason;
    private String additionalInfo;
}
