package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 15:16
 **/

@Builder
@Data
@AllArgsConstructor
public class SyncTimeResponse {

    private String pileCode;

    private Instant currentTime;
}
