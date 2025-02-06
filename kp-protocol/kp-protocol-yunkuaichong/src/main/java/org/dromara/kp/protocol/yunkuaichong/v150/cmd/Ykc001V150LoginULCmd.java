
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.LoginRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

import java.nio.charset.StandardCharsets;

/**
 * 云快充1.5.0充电桩登录认证
 */
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.LOGIN)
public class Ykc001V150LoginULCmd extends YunKuaiChongUplinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0登录认证请求", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();

        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        int pileType = byteBuf.readUnsignedByte();
        additionalInfo.put("桩类型(0直流1交流)", pileType);

        int gunsNum = byteBuf.readUnsignedByte();
        additionalInfo.put("充电枪数量", gunsNum);
        additionalInfo.put("通信协议版本", byteBuf.readUnsignedByte());
        byte[] bytes = new byte[8];
        byteBuf.readBytes(bytes);
        additionalInfo.put("程序版本", new String(bytes, StandardCharsets.US_ASCII));

        int netType = byteBuf.readUnsignedByte();
        additionalInfo.put("网络链接类型 00_SIM 01_LAN 02_WAN 03_OTHER", netType);

        byte[] simB = new byte[10];
        byteBuf.readBytes(simB);
        String sim = BCDUtil.toString(simB);
        additionalInfo.put("Sim卡", sim);
        additionalInfo.put("运营商 00移动 02电信 03联通 04其他", byteBuf.readUnsignedByte());

        tcpSession.addPileCode(pileCode);

        // 注册前置会话
        ctx.getProtocolSessionRegistryProvider().register(tcpSession);

        // 转发到后端
        LoginRequest loginRequest = LoginRequest.builder()
            .pileCode(pileCode)
            .gunsNum(gunsNum)
            .netType(netType)
            .pileType(pileType)
            .build();

        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(loginRequest.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
            .loginRequest(loginRequest)
            .build();

        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);
        log.info("{} 登录认证请求 : {}",pileCode,additionalInfo);
    }

}
