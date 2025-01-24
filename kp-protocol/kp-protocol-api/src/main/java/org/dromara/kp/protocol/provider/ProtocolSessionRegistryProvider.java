
package org.dromara.kp.protocol.provider;


import org.dromara.kp.protocol.domain.ProtocolSession;

import java.util.UUID;

/**
 * @author but
 */
public interface ProtocolSessionRegistryProvider {

    /**
     * 注册会话
     */
    void register(ProtocolSession protocolSession);

    void unregister(UUID sessionId);

    ProtocolSession get(UUID sessionId);

    /**
     * 活跃会话
     */
    void activate(ProtocolSession protocolSession);
}
