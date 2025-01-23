package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DownlinkResponseMessage {
    private boolean success;
    private String error;
}
