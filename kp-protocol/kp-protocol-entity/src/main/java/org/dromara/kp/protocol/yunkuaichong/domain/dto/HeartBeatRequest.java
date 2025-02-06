package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class HeartBeatRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String pileCode;
    private String remoteAddress;
    private String nodeId;
    private String nodeHostAddress;
    private int nodeRestPort;
    private int nodeGrpcPort;
    private String additionalInfo;
}
