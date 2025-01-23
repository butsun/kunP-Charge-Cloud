package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RemoteStartChargingResponse {
    private long ts;
    private String pileCode;
    private String gunCode;
    private String tradeNo;
    private boolean success;
    private String failReason;
    private String additionalInfo;
}
