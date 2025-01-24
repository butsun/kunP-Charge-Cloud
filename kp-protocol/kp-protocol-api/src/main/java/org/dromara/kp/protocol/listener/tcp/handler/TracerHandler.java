
package org.dromara.kp.protocol.listener.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.dromara.kp.infrastructure.util.mdc.MDCUtils;
import org.dromara.kp.infrastructure.util.trace.TracerContextUtil;


/**
 * @author but
 */
public class TracerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        TracerContextUtil.newTracer("jcpp-protocol");

        MDCUtils.recordTracer();

        super.channelRead(ctx, msg);
    }
}
