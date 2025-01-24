
package org.dromara.kp.protocol.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.DownlinkRequestMessage;

/**
 * @author but
 */
@Data
@AllArgsConstructor
public class SessionToHandlerMsg{

    private DownlinkRequestMessage downlinkMsg;
    private ProtocolSession session;


}
