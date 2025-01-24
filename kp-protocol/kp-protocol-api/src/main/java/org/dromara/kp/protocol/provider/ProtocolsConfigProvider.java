
package org.dromara.kp.protocol.provider;


import org.dromara.kp.protocol.cfg.ProtocolCfg;

/**
 * @author but
 */
public interface ProtocolsConfigProvider {

    ProtocolCfg loadConfig(String protocol);
}
