package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class RequestMsg  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long ts;
    private TracerProto tracer;
    private ConnectRequestMsg connectRequestMsg;
    private DownlinkRequestMessage downlinkRequestMessage;
}
