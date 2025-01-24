
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.HeartBeatRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.HEARTBEAT;

/**
 * 云快充1.5.0 充电桩心跳包
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(0x03)
public class YunKuaiChongV150HeartbeatULCmd extends YunKuaiChongUplinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0充电桩心跳包", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();

        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        byte gunCodeByte = byteBuf.readByte();
        int gunCode = Integer.parseInt(BCDUtil.toString(gunCodeByte));
        additionalInfo.put("枪号", gunCode);

        int gunState = byteBuf.readUnsignedByte();
        additionalInfo.put("枪状态(0正常 1故障)", gunState);

        // 刷新前置会话
        ctx.getProtocolSessionRegistryProvider().activate(tcpSession);

        // 转发到后端
        HeartBeatRequest heartBeatRequest = HeartBeatRequest.builder()
                .pileCode(pileCode)
                .remoteAddress(tcpSession.getAddress().toString())
                .additionalInfo(additionalInfo.toString())
                .build();
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(heartBeatRequest.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
                .heartBeatRequest(heartBeatRequest)
                .build();
//        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);

        pingAck(tcpSession, pileCodeBytes, gunCodeByte);
    }

    private void pingAck(TcpSession tcpSession, byte[] pileCodeBytes, byte gunCodeByte) {
        ByteBuf pingAckMsgBody = Unpooled.buffer(9);
        pingAckMsgBody.writeBytes(pileCodeBytes);
        pingAckMsgBody.writeByte(gunCodeByte);
        pingAckMsgBody.writeByte(0);

        encodeAndWriteFlush(HEARTBEAT,
                pingAckMsgBody,
                tcpSession);
    }
}
