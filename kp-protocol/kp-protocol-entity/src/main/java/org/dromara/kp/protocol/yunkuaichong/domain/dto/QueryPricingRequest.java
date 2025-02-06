package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class QueryPricingRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String pileCode;
}
