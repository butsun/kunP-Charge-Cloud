/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.cfg;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.dromara.kp.protocol.cfg.enums.ForwarderType;

@Setter
@Getter
public class ForwarderCfg {

    @NotNull
    private ForwarderType type;

    private MemoryCfg memory;

    @Valid
    private KafkaCfg kafka;
}
