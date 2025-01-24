
package org.dromara.kp.protocol.listener;

import lombok.Getter;
import org.dromara.kp.protocol.ProtocolMessageProcessor;
import org.springframework.boot.actuate.health.Health;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author but
 */
public abstract class Listener {

    @Getter
    private final String protocolName;

    @Getter
    private final ProtocolMessageProcessor protocolMessageProcessor;

    protected AtomicInteger connectionsGauge = new AtomicInteger();
//    protected MessagesStats uplinkMsgStats;
//    protected MessagesStats downlinkMsgStats;
//    protected DefaultCounter uplinkTrafficCounter;
//    protected DefaultCounter downlinkTrafficCounter;
//    protected Timer downlinkTimer;

    protected final ChannelHandlerParameter parameter;

    protected Listener(String protocolName, ProtocolMessageProcessor protocolMessageProcessor) {
        this.protocolName = protocolName;
        this.protocolMessageProcessor = protocolMessageProcessor;

//        statsFactory.createGauge("openConnections", connectionsGauge, "protocol", protocolName);
//        this.uplinkMsgStats = statsFactory.createMessagesStats("listenerUplinkMessage", "protocol", protocolName);
//        this.downlinkMsgStats = statsFactory.createMessagesStats("listenerDownlinkMessage", "protocol", protocolName);
//        this.uplinkTrafficCounter = statsFactory.createDefaultCounter("listenerUplinkTraffic", "protocol", protocolName);
//        this.downlinkTrafficCounter = statsFactory.createDefaultCounter("listenerDownlinkTraffic", "protocol", protocolName);
//        this.downlinkTimer = statsFactory.createTimer("listenerDownlink", "protocol", protocolName);

        this.parameter = new ChannelHandlerParameter(protocolName, protocolMessageProcessor, connectionsGauge);
    }

    public abstract Health health();

    public abstract void destroy() throws InterruptedException;
}
