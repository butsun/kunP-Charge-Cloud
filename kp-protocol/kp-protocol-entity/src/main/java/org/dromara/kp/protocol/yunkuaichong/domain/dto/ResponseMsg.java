package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMsg {
    private TracerProto tracer;
    private ConnectResponseMsg connectResponseMsg;
    private DownlinkResponseMessage downlinkResponseMsg;
}
