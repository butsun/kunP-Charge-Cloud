package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TestMsg {
    private long ts;
    private long st;
}
