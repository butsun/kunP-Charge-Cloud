
package org.dromara.kp.protocol;

import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.exception.DownlinkException;
import org.dromara.kp.infrastructure.util.trace.TracerRunnable;
import org.dromara.kp.protocol.domain.ListenerToHandlerMsg;
import org.dromara.kp.protocol.domain.SessionToHandlerMsg;
import org.dromara.kp.protocol.forwarder.Forwarder;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileLostEvent;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;


import java.util.UUID;

/**
 * @author but
 */
@Slf4j
public abstract class ProtocolMessageProcessor {
    protected final Forwarder forwarder;
    protected final ProtocolContext protocolContext;

    protected ProtocolMessageProcessor(Forwarder forwarder, ProtocolContext protocolContext) {
        this.forwarder = forwarder;
        this.protocolContext = protocolContext;
    }

    public void uplinkHandleAsync(ListenerToHandlerMsg listenerToHandlerMsg) {

        UUID id = listenerToHandlerMsg.getSession().getId();

        protocolContext.getShardingThreadPool().execute(id, new TracerRunnable(() -> {
            try {

                listenerToHandlerMsg.getSession().setForwarder(forwarder);

                uplinkHandle(listenerToHandlerMsg);

            } catch (Exception e) {


                log.error("{} 消息处理器处理报文异常", listenerToHandlerMsg.getSession(), e);
            }
        }));
    }

    protected abstract void uplinkHandle(ListenerToHandlerMsg listenerToHandlerMsg);

    public void downlinkHandleStream(SessionToHandlerMsg sessionToHandlerMsg) throws DownlinkException {
        try {
            downlinkHandle(sessionToHandlerMsg);
        } catch (Exception e) {

            throw new DownlinkException(e.getMessage(), e);
        }
    }

    protected abstract void downlinkHandle(SessionToHandlerMsg sessionToHandlerMsg);


    public void sessionClose(UUID uuid) {
        protocolContext.getProtocolSessionRegistryProvider().unregister(uuid);
    }
}
