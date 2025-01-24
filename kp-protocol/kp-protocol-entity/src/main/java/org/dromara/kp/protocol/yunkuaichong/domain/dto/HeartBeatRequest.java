package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HeartBeatRequest {
    private String pileCode;
    private String remoteAddress;
    private String nodeId;
    private String nodeHostAddress;
    private int nodeRestPort;
    private int nodeGrpcPort;
    private String additionalInfo;
}
