package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TracerProto {
    private String id;
    private String origin;
    private long ts;
}
