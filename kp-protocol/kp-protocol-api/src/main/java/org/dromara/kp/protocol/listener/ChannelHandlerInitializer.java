/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.listener;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.protocol.cfg.TcpCfg;
import org.dromara.kp.protocol.cfg.TcpHandlerCfg;
import org.dromara.kp.protocol.cfg.enums.TcpHandlerType;
import org.dromara.kp.protocol.listener.tcp.TcpChannelHandler;
import org.dromara.kp.protocol.listener.tcp.configs.BinaryHandlerConfiguration;
import org.dromara.kp.protocol.listener.tcp.configs.TextHandlerConfiguration;
import org.dromara.kp.protocol.listener.tcp.decoder.JCPPHeadTailFrameDecoder;
import org.dromara.kp.protocol.listener.tcp.decoder.JCPPLengthFieldBasedFrameDecoder;
import org.dromara.kp.protocol.listener.tcp.decoder.TcpMsgDecoder;
import org.dromara.kp.protocol.listener.tcp.handler.ConnectionLimitHandler;
import org.dromara.kp.protocol.listener.tcp.handler.IdleEventHandler;
import org.dromara.kp.protocol.listener.tcp.handler.TracerHandler;


import java.nio.ByteOrder;

import static org.dromara.kp.protocol.cfg.enums.TcpHandlerType.BINARY;
import static org.dromara.kp.protocol.cfg.enums.TcpHandlerType.TEXT;
import static org.dromara.kp.protocol.listener.tcp.configs.BinaryHandlerConfiguration.LITTLE_ENDIAN_BYTE_ORDER;
import static org.dromara.kp.protocol.listener.tcp.configs.TextHandlerConfiguration.SYSTEM_LINE_SEPARATOR;

/**
 * @author baigod
 */
@Slf4j
@RequiredArgsConstructor
public abstract class ChannelHandlerInitializer<C extends Channel> extends ChannelInitializer<C> {

    protected final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected abstract void initChannel(C ch);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    public static ChannelHandlerInitializer<SocketChannel> createTcpChannelHandler(TcpCfg tcpCfg, ChannelHandlerParameter parameter) {
        TcpHandlerCfg tcpCfgHandler = tcpCfg.getHandler();
        TcpHandlerType type = tcpCfgHandler.getType();


        return switch (type) {
            case TEXT -> new ChannelHandlerInitializer<>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    TextHandlerConfiguration textHandlerConfig = (TextHandlerConfiguration) tcpCfgHandler.getConfiguration(TEXT);
                    ByteBuf[] delimiters = SYSTEM_LINE_SEPARATOR.equals(textHandlerConfig.getMessageSeparator())
                        ? Delimiters.lineDelimiter() : Delimiters.nulDelimiter();
                    DelimiterBasedFrameDecoder framer = new DelimiterBasedFrameDecoder(textHandlerConfig.getMaxFrameLength(),
                        textHandlerConfig.isStripDelimiter(), delimiters);
                    socketChannel.pipeline()
                        .addLast("tracerHandler", new TracerHandler())
                        .addLast("connectionLimitHandler", new ConnectionLimitHandler(parameter.getProtocolName(), tcpCfgHandler.getMaxConnections(), CHANNEL_GROUP, parameter.getConnectionsGauge()))
                        .addLast("idleStateHandler", new IdleStateHandler(tcpCfgHandler.getIdleTimeoutSeconds(), 0, 0))
                        .addLast("idleEventHandler", new IdleEventHandler(parameter.getProtocolName()))
                        .addLast("framer", framer)
                        .addLast("tcpTextDecoder", new TcpMsgDecoder<>(parameter.getProtocolName(), msg -> TcpMsgDecoder.toString(msg, textHandlerConfig.getCharsetName())))
                        .addLast("tcpStringInHandler", new TcpChannelHandler<>(parameter));

                }
            };
            case JSON -> new ChannelHandlerInitializer<>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline()
                        .addLast("tracerHandler", new TracerHandler())
                        .addLast("connectionLimitHandler", new ConnectionLimitHandler(parameter.getProtocolName(), tcpCfgHandler.getMaxConnections(), CHANNEL_GROUP, parameter.getConnectionsGauge()))
                        .addLast("idleStateHandler",
                            new IdleStateHandler(tcpCfgHandler.getIdleTimeoutSeconds(), 0, 0))
                        .addLast("idleEventHandler", new IdleEventHandler(parameter.getProtocolName()))
                        .addLast("datagramToJsonDecoder", new JsonObjectDecoder())
                        .addLast("tcpJsonDecoder", new TcpMsgDecoder<>(parameter.getProtocolName(), TcpMsgDecoder::toJson))
                        .addLast("tcpJsonInHandler", new TcpChannelHandler<>(parameter));
                }
            };
            case BINARY -> new ChannelHandlerInitializer<>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    BinaryHandlerConfiguration binaryHandlerConfig = (BinaryHandlerConfiguration) tcpCfgHandler.getConfiguration(BINARY);

                    ByteOrder byteOrder = LITTLE_ENDIAN_BYTE_ORDER.equalsIgnoreCase(binaryHandlerConfig.getByteOrder())
                        ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;

                    socketChannel.pipeline()
                        .addLast("tracerHandler", new TracerHandler())
                        .addLast("connectionLimitHandler", new ConnectionLimitHandler(parameter.getProtocolName(), tcpCfgHandler.getMaxConnections(), CHANNEL_GROUP, parameter.getConnectionsGauge()))
                        .addLast("idleStateHandler", new IdleStateHandler(tcpCfgHandler.getIdleTimeoutSeconds(), 0, 0))
                        .addLast("idleEventHandler", new IdleEventHandler(parameter.getProtocolName()));

                    if (LengthFieldBasedFrameDecoder.class.isAssignableFrom(binaryHandlerConfig.getDecoder())) {
                        LengthFieldBasedFrameDecoder framer = new LengthFieldBasedFrameDecoder(byteOrder,
                            binaryHandlerConfig.getMaxFrameLength(), binaryHandlerConfig.getLengthFieldOffset(),
                            binaryHandlerConfig.getLengthFieldLength(), binaryHandlerConfig.getLengthAdjustment(),
                            binaryHandlerConfig.getInitialBytesToStrip(), binaryHandlerConfig.isFailFast());
                        socketChannel.pipeline().addLast("LengthFieldBasedFrameDecoder", framer);
                    } else if (JCPPLengthFieldBasedFrameDecoder.class.isAssignableFrom(binaryHandlerConfig.getDecoder())) {
                        JCPPLengthFieldBasedFrameDecoder framer = new JCPPLengthFieldBasedFrameDecoder(binaryHandlerConfig.getHead(), byteOrder,
                            binaryHandlerConfig.getLengthFieldOffset(), binaryHandlerConfig.getLengthFieldLength(),
                            binaryHandlerConfig.getLengthAdjustment(), binaryHandlerConfig.getInitialBytesToStrip());
                        socketChannel.pipeline().addLast("JCPPLengthFieldBasedFrameDecoder", framer);
                    } else if (JCPPHeadTailFrameDecoder.class.isAssignableFrom(binaryHandlerConfig.getDecoder())) {
                        JCPPHeadTailFrameDecoder framer = new JCPPHeadTailFrameDecoder(binaryHandlerConfig.getHead(),
                            binaryHandlerConfig.getTail());
                        socketChannel.pipeline().addLast("JCPPHeadTailFrameDecoder", framer);
                    } else {
                        throw new IllegalArgumentException("Unknown binary decoder");
                    }

                    socketChannel.pipeline()
                        .addLast("tcpByteDecoderOverride", new TcpMsgDecoder<>(parameter.getProtocolName(), TcpMsgDecoder::toByteArray))
                        .addLast("tcpByteHandler", new TcpChannelHandler<>(parameter));
                }
            };
            default -> throw new IllegalArgumentException("Unknown: " + tcpCfgHandler);
        };

    }
}
