
package org.dromara.kp.protocol.cfg;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListenerCfg {

    @Valid
    private TcpCfg tcp;
}
