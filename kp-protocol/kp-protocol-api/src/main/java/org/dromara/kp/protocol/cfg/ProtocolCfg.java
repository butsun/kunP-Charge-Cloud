package org.dromara.kp.protocol.cfg;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProtocolCfg {

    private boolean enabled;

    @NotNull
    @Valid
    private ListenerCfg listener;


}
