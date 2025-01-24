package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class VerifyPricingRequest {
    private String pileCode;
    private long pricingId;
    private String pricingModel;
    private String additionalInfo;
}
