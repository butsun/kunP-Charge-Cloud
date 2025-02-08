package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:05
 **/
@Data
@AllArgsConstructor
@Builder
public class PileLostEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Set<String> pileCode;
}
