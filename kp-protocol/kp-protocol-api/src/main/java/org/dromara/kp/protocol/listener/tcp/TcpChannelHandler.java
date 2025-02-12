
package org.dromara.kp.protocol.listener.tcp;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.exception.DownlinkException;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolMessageProcessor;
import org.dromara.kp.protocol.domain.ListenerToHandlerMsg;
import org.dromara.kp.protocol.domain.ProtocolUplinkMsg;
import org.dromara.kp.protocol.domain.SessionCloseReason;
import org.dromara.kp.protocol.domain.SessionToHandlerMsg;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.DownlinkRequestMessage;
import org.dromara.kp.protocol.listener.ChannelHandlerParameter;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileLostEvent;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class TcpChannelHandler<T> extends SimpleChannelInboundHandler<ProtocolUplinkMsg<T>> {
    private final String protocolName;
    private final ProtocolMessageProcessor protocolMessageProcessor;

    private final TcpSession tcpSession;

    @SneakyThrows
    public TcpChannelHandler(ChannelHandlerParameter parameter) {
        this.protocolName = parameter.getProtocolName();
        this.protocolMessageProcessor = parameter.getProtocolMessageProcessor();
        tcpSession = new TcpSession(protocolName, this::onDownlink, this::writeAndFlush);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolUplinkMsg<T> msg) {

        if (log.isDebugEnabled()) {

            log.debug("[{}]{}{} Netty拆出到上行报文:{}", protocolName, ctx.channel(), tcpSession, msg);
        }


        tcpSession.setLastActivityTime(LocalDateTime.now());

        if (tcpSession.getAddress() == null) {

            tcpSession.setAddress(msg.getAddress());
        }

        if (tcpSession.getCtx() == null) {

            tcpSession.setCtx(ctx);
        }

        T data = msg.getData();

        if (Objects.isNull(data)) {

            log.debug("[{}]{}{} 上行报文为空被过滤 [{}]", protocolName, ctx.channel(), tcpSession, msg);

            return;
        }

        try {

            process(msg, ctx);


        } catch (Exception e) {

            log.error("[{}]{}{} TCP管道处理报文异常", protocolName, ctx.channel(), tcpSession, e);
        }
    }

    private void process(ProtocolUplinkMsg<T> msg, ChannelHandlerContext ctx) {
        Object data = msg.getData();
        if (data instanceof byte[]) {
            protocolMessageProcessor.uplinkHandleAsync(new ListenerToHandlerMsg(msg.getId(), (byte[]) data, tcpSession));
        } else if (data instanceof JsonNode) {
            protocolMessageProcessor.uplinkHandleAsync(new ListenerToHandlerMsg(msg.getId(), JacksonUtil.writeValueAsBytes((JsonNode) data), tcpSession));
        } else if (data instanceof String) {
            protocolMessageProcessor.uplinkHandleAsync(new ListenerToHandlerMsg(msg.getId(), JacksonUtil.writeValueAsBytes(((String) data).getBytes()), tcpSession));
        } else {
            assert data != null;
            log.warn("[{}]{}{} 不支持的TCP上行报文类型:{}", protocolName, ctx.channel(), tcpSession, data.getClass());
        }
    }

    protected void onDownlink(DownlinkRequestMessage downlinkMsg) throws DownlinkException {
        protocolMessageProcessor.downlinkHandleStream(new SessionToHandlerMsg(downlinkMsg, tcpSession));
    }

    protected void writeAndFlush(ByteBuf... byteBufList) {
        if (byteBufList == null || byteBufList.length == 0) {

            return;
        }

        ChannelHandlerContext ctx = tcpSession.getCtx();

        if (ctx.isRemoved()) {

            tcpSession.close(SessionCloseReason.INACTIVE);

            log.warn("[{}]{}{} TCP会话已失效，因此删除会话", protocolName, ctx.channel(), tcpSession);

            return;
        }


        for (ByteBuf byteBuf : byteBufList) {

            try {

                if (Objects.isNull(byteBuf)) {
                    log.warn("[{}]{}{} 下发空报文被拦截", protocolName, ctx.channel(), tcpSession);
                    continue;
                }

                logDownlinkStart(byteBuf.readableBytes(), () -> ByteBufUtil.hexDump(byteBuf));

                ctx.writeAndFlush(Unpooled.wrappedBuffer(byteBuf))
                    .addListener(this::logDownlinkUnsuccessful);


            } catch (Exception e) {

                throw e;
            }
        }

    }

    private void logDownlinkStart(int payloadSize, Supplier<String> logTransform) {
        if (log.isDebugEnabled()) {
            log.debug("[{}]{} 开始发送下行报文:{}", protocolName, tcpSession, logTransform.get());
        }
    }

    private void logDownlinkUnsuccessful(Future<? super Void> channelFuture) {
        if (channelFuture.isDone() && !channelFuture.isSuccess()) {
            log.info("[{}]{} 下行报文发送未成功", protocolName, tcpSession);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

        ctx.flush();
        if (log.isTraceEnabled()) {
            log.trace("[{}]{}{} Channel Read Complete [{}]", protocolName, ctx.channel(), tcpSession, ctx.name());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[{}]{}{} Invalid message received, Exception caught", protocolName, ctx.channel(), tcpSession, cause);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);

        log.info("[{}]{} 打开通道", protocolName, ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

        super.channelUnregistered(ctx);
        close();
        log.info("[{}]{}{} 关闭通道", protocolName, ctx.channel(), tcpSession);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);

        log.info("[{}]{} 通道活跃", protocolName, ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("[{}]{}{} 通道不活跃", protocolName, ctx.channel(), tcpSession);
    }


    private void close() {
        protocolMessageProcessor.sessionClose(tcpSession.getId());
    }

}
