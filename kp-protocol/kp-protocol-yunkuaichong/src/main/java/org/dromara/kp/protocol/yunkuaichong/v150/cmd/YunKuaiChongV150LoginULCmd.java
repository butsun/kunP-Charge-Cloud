
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.codec.CP56Time2aUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.infrastructure.util.mdc.MDCUtils;
import org.dromara.kp.infrastructure.util.trace.TracerContextUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.LoginRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.listener.tcp.enums.SequenceNumberLength;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.dromara.kp.infrastructure.util.config.ThreadPoolConfiguration.PROTOCOL_SESSION_SCHEDULED;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.FAILURE_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.SUCCESS_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.LOGIN_ACK;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.SYNC_TIME;

/**
 * 云快充1.5.0充电桩登录认证
 */
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.LOGIN)
public class YunKuaiChongV150LoginULCmd extends YunKuaiChongUplinkCmdExe {

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
        additionalInfo.put("网络链接类型", byteBuf.readUnsignedByte());

        byte[] simB = new byte[10];
        byteBuf.readBytes(simB);
        String sim = BCDUtil.toString(simB);
        additionalInfo.put("Sim卡", sim);
        additionalInfo.put("运营商", byteBuf.readUnsignedByte());

        tcpSession.addPileCode(pileCode);

        // 注册前置会话
        ctx.getProtocolSessionRegistryProvider().register(tcpSession);

        // 转发到后端
        LoginRequest loginRequest = LoginRequest.builder()
                .pileCode(pileCode)
                .credential(pileCode)
                .remoteAddress(tcpSession.getAddress().toString())
                .additionalInfo(additionalInfo.toString())
                .build();
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(loginRequest.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
                .loginRequest(loginRequest)
                .build();
//        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);


        //TODO 调试用   必须登录成功
        YunKuaiChongUplinkMessage requestData = JacksonUtil.fromBytes(uplinkQueueMessage.getRequestData(), YunKuaiChongUplinkMessage.class);
        // 构造并下发登录ACK
        loginAck(tcpSession, pileCodeBytes, requestData, true);
        // 构造定时对时
        registerSyncTimeTask(tcpSession, pileCodeBytes, requestData);
    }


    private void loginAck(TcpSession tcpSession, byte[] pileCodeBytes, YunKuaiChongUplinkMessage requestData, boolean loginSuccess) {
        // 创建ACK消息体7字节桩编号+1字节登录结果
        ByteBuf loginAckMsgBody = Unpooled.buffer(8);
        loginAckMsgBody.writeBytes(pileCodeBytes);
        loginAckMsgBody.writeByte(loginSuccess ? SUCCESS_BYTE : FAILURE_BYTE);

        encodeAndWriteFlush(LOGIN_ACK,
            requestData.getSequenceNumber(),
            requestData.getEncryptionFlag(),
            loginAckMsgBody,
            tcpSession);
    }

    private void registerSyncTimeTask(TcpSession tcpSession, byte[] pileCodeBytes, YunKuaiChongUplinkMessage requestData) {
        tcpSession.addSchedule("auto-sync-time", k -> {
                log.info("{} 云快充1.5.0开始注册定时对时任务", tcpSession);
                return PROTOCOL_SESSION_SCHEDULED.scheduleAtFixedRate(() ->
                        syncTime(tcpSession, pileCodeBytes, requestData),
                    0, RandomUtil.randomInt(420, 480), TimeUnit.MINUTES);
            }
        );
    }

    private void syncTime(TcpSession tcpSession, byte[] pileCodeBytes, YunKuaiChongUplinkMessage requestData) {
        TracerContextUtil.newTracer();
        MDCUtils.recordTracer();
        log.info("{} 云快充1.5.0开始下发对时报文", tcpSession);
        ByteBuf syncTimeMsgBody = Unpooled.buffer(14);
        syncTimeMsgBody.writeBytes(pileCodeBytes);
        syncTimeMsgBody.writeBytes(CP56Time2aUtil.encode(Instant.now()));

        encodeAndWriteFlush(SYNC_TIME,
            tcpSession.nextSeqNo(SequenceNumberLength.SHORT),
            requestData.getEncryptionFlag(),
            syncTimeMsgBody,
            tcpSession);
    }
}
