package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class QueryPricingResponse {
    private String pileCode;
    private long pricingId;
    private PricingModelProto pricingModel;
}
