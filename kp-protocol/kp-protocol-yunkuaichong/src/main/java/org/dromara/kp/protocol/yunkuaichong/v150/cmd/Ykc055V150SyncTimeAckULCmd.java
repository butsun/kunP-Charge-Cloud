package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.codec.CP56Time2aUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.RemoteStartChargingResponse;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.SyncTimeResponse;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;

import java.time.Instant;

import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.LOGIN_ACK;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.SYNC_TIME;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 15:13
 **/

@Slf4j
@YunKuaiChongCmd(downCmd = SYNC_TIME)
public class Ykc055V150SyncTimeAckULCmd extends YunKuaiChongUplinkCmdExe {


    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0对时应答", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        // 2.桩编号
        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);


        // 2.桩编号
        byte[] currentTimeBytes = new byte[7];
        byteBuf.readBytes(currentTimeBytes);
        Instant currentTime = CP56Time2aUtil.decode(currentTimeBytes);

        SyncTimeResponse syncTimeResponse = SyncTimeResponse.builder()
            .pileCode(pileCode)
            .currentTime(currentTime)
            .build();

        // 转发到后端
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(pileCode, tcpSession, yunKuaiChongUplinkMessage)
            .syncTimeResponse(syncTimeResponse)
            .build();

        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);
        log.debug("{} 对时应答 {}", pileCode, syncTimeResponse);
    }
}
