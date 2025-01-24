
package org.dromara.kp.protocol.provider.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.config.ConstraintValidator;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.cfg.ProtocolCfg;
import org.dromara.kp.protocol.provider.ProtocolsConfigProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;


import java.util.Map;

@Setter
@Service
@Slf4j
@ConfigurationProperties("service")
public class DefaultProtocolsConfigProvider implements ProtocolsConfigProvider {

    private Map<String, ProtocolCfg> protocols;

    @Override
    public ProtocolCfg loadConfig(String protocol) {

        ProtocolCfg protocolCfg = protocols.get(protocol);

        log.info("load {}'s configuration: \n{}", protocol, JacksonUtil.toPrettyString(protocolCfg));

        ConstraintValidator.validateFields(protocolCfg, "'" + protocol + "' configuration is invalid:");

        return protocolCfg;
    }
}
