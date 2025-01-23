/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.forwarder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;


/**
 *
 *
 *  改用dubbo
 * @author baigod
 */
@Slf4j
public abstract class Forwarder {
    protected static final String ERROR = "error";

    AtomicBoolean healthy = new AtomicBoolean(true);

    @Getter
    private final String protocolName;


//    protected final PartitionProvider partitionProvider;
//    protected final ServiceInfoProvider serviceInfoProvider;

//    protected final boolean isMonolith;
//    protected QueueProducer<ProtoQueueMsg<UplinkQueueMessage>> producer;

    protected Forwarder(String protocolName) {
        this.protocolName = protocolName;
//        this.partitionProvider = partitionProvider;
//        this.serviceInfoProvider = serviceInfoProvider;
//
//        this.forwarderMessagesStats = statsFactory.createMessagesStats("forwarderMessages", "protocol", protocolName);
//
//        this.isMonolith = serviceInfoProvider.isMonolith();
    }

    public abstract Health health();

    public abstract void destroy();

    protected void jcppForward(String topic, String key, Object msg, BiConsumer<Boolean, ObjectNode> consumer) {

    }

    public abstract void sendMessage(Object msg, BiConsumer<Boolean, ObjectNode> consumer);

    public abstract void sendMessage(Object msg);

}
