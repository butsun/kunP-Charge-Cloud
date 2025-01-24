package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoteStartChargingRequest {
    private String pileCode;
    private String gunCode;
    private String tradeNo;
    private String limitYuan;
    private String additionalInfo;
}
