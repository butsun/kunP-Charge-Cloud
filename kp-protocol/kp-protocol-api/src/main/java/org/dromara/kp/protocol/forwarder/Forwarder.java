
package org.dromara.kp.protocol.forwarder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.springframework.boot.actuate.health.Health;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;


/**
 *
 *
 *  改用dubbo
 * @author but
 */
@Slf4j
public abstract class Forwarder {
    protected static final String ERROR = "error";

    public abstract void sendMessage(UplinkQueueMessage msg, BiConsumer<Boolean, ObjectNode> consumer);

    public abstract void sendMessage(UplinkQueueMessage msg);

}
