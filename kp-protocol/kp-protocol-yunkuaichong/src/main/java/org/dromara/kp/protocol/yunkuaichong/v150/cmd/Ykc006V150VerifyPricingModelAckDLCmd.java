
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.VerifyPricingResponse;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import java.util.Arrays;

import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.FAILURE_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.SUCCESS_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.VERIFY_PRICING_ACK;

/**
 * 云快充1.5.0计费模型验证请求应答
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(downCmd = VERIFY_PRICING_ACK)
public class Ykc006V150VerifyPricingModelAckDLCmd extends YunKuaiChongDownlinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0计费模型验证请求应答 {}", tcpSession,yunKuaiChongDwonlinkMessage.getMsg().getVerifyPricingResponse());

        if (yunKuaiChongDwonlinkMessage.getMsg().getVerifyPricingResponse() == null) {
            return;
        }

        VerifyPricingResponse verifyPricingResponse = yunKuaiChongDwonlinkMessage.getMsg().getVerifyPricingResponse();
        YunKuaiChongUplinkMessage requestData = JacksonUtil.fromBytes(yunKuaiChongDwonlinkMessage.getMsg().getRequestData(), YunKuaiChongUplinkMessage.class);

        // 创建ACK消息体7字节桩编号+2字节计费模型编号+1字节验证结果
        ByteBuf verifyPricingAckMsgBody = Unpooled.buffer(10);
        verifyPricingAckMsgBody.writeBytes(encodePileCode(yunKuaiChongDwonlinkMessage.getMsg().getPileCode()));
        verifyPricingAckMsgBody.writeBytes(encodePricingId(verifyPricingResponse.getPricingId()));
        verifyPricingAckMsgBody.writeByte(verifyPricingResponse.isSuccess() ? SUCCESS_BYTE : FAILURE_BYTE);

        encodeAndWriteFlush(VERIFY_PRICING_ACK,
            requestData.getSequenceNumber(),
            requestData.getEncryptionFlag(),
            verifyPricingAckMsgBody,
            tcpSession);
    }
}
