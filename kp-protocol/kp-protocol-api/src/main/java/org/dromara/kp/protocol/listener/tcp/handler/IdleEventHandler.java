
package org.dromara.kp.protocol.listener.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳检测
 *
 * @author but
 */
@Slf4j
@RequiredArgsConstructor
public class IdleEventHandler extends ChannelInboundHandlerAdapter {

    private final String protocolName;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.READER_IDLE) {
                ctx.close();
                log.info("[{}]{} 检测到空闲连接，连接关闭", protocolName, ctx.channel());
            }
        }
    }

}
