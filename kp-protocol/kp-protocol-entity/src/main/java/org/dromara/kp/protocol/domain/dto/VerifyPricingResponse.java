package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyPricingResponse {
    private boolean success;
    private long pricingId;
}
