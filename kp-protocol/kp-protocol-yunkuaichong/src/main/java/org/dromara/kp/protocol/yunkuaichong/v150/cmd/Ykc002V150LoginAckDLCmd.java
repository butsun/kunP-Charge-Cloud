
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import cn.hutool.core.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.CP56Time2aUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.infrastructure.util.mdc.MDCUtils;
import org.dromara.kp.infrastructure.util.trace.TracerContextUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.listener.tcp.enums.SequenceNumberLength;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.LoginResponse;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.dromara.kp.infrastructure.util.config.ThreadPoolConfiguration.PROTOCOL_SESSION_SCHEDULED;
import static org.dromara.kp.protocol.domain.SessionCloseReason.MANUALLY;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.FAILURE_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.SUCCESS_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.LOGIN_ACK;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.SYNC_TIME;

/**
 * 云快充1.5.0登录认证应答
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(downCmd = LOGIN_ACK)
public class Ykc002V150LoginAckDLCmd extends YunKuaiChongDownlinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0登录认证应答", tcpSession);

        if (Objects.equals(yunKuaiChongDwonlinkMessage.getMsg().getLoginResponse(),null)) {
            return;
        }

        LoginResponse loginResponse = yunKuaiChongDwonlinkMessage.getMsg().getLoginResponse();

        YunKuaiChongUplinkMessage requestData = JacksonUtil.fromBytes(yunKuaiChongDwonlinkMessage.getMsg().getRequestData(), YunKuaiChongUplinkMessage.class);

        // 获取上行报文
        byte[] uplinkRawFrame = requestData.getRawFrame();
        // 从上行报文中取出桩编号字节数组
        byte[] pileCodeBytes = Arrays.copyOfRange(uplinkRawFrame, 6, 13);

        if (loginResponse.isSuccess()) {

            // 构造并下发登录ACK
            loginAck(tcpSession, pileCodeBytes, requestData, true);

            // 构造定时对时
            registerSyncTimeTask(tcpSession, pileCodeBytes, requestData);

        } else {

            log.info("云快充V1.5登录认证失败，服务端断开连接。 pileCode:{}", loginResponse.getPileCode());

            // 构造并下发登录ACK
            loginAck(tcpSession, pileCodeBytes, requestData, false);

            // 断开连接
            tcpSession.close(MANUALLY);
        }
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
