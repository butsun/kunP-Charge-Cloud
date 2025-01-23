package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestMsg {
    private long ts;
    private TracerProto tracer;
    private ConnectRequestMsg connectRequestMsg;
    private DownlinkRequestMessage downlinkRequestMessage;
}
