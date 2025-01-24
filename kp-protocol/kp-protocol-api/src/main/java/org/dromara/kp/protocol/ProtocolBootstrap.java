
package org.dromara.kp.protocol;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.protocol.cfg.ProtocolCfg;
import org.dromara.kp.protocol.cfg.TcpCfg;
import org.dromara.kp.protocol.cfg.enums.ForwarderType;
import org.dromara.kp.protocol.forwarder.Forwarder;
import org.dromara.kp.protocol.listener.Listener;
import org.dromara.kp.protocol.listener.tcp.TcpListener;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;


import static org.springframework.boot.actuate.health.Status.UP;

/**
 * @author but
 */
@Slf4j
public abstract class ProtocolBootstrap implements HealthIndicator {

    @Resource
    protected ProtocolContext protocolContext;

    protected ProtocolCfg protocolCfg;

    protected Listener listener;

//    @Resource
    protected Forwarder forwarder;

    @PostConstruct
    public void init() throws InterruptedException {
        String protocolName = getProtocolName();

        log.info("Protocol Service [{}] Initializing...", protocolName);

        protocolCfg = protocolContext.getProtocolsConfigProvider().loadConfig(protocolName);

        TcpCfg tcpCfg = protocolCfg.getListener().getTcp();

        if (tcpCfg != null) {

            listener = new TcpListener(protocolName, tcpCfg, messageProcessor());
        }

        _init();
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        log.info("{} destroy...", getProtocolName());

        if (listener != null) {
            listener.destroy();
        }

        if (forwarder != null) {
            forwarder.destroy();
        }

        _destroy();
    }


    @Override
    public Health health() {
        Health.Builder healthBuilder;

        if (listener != null && listener.health().getStatus() == UP) {
            healthBuilder = Health.up();
        } else {
            healthBuilder = Health.down();
        }

        if (listener != null) {
            healthBuilder.withDetail("listener", listener.health().getStatus());
        }

        if (forwarder != null) {
            healthBuilder.withDetail("forwarder", forwarder.health().getStatus());
        }

        return healthBuilder.build();
    }


    protected abstract String getProtocolName();

    protected abstract void _init();

    protected abstract void _destroy();

    protected abstract ProtocolMessageProcessor messageProcessor();

}
