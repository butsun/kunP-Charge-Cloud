/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;


import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.domain.dto.VerifyPricingRequest;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.FAILURE_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.SUCCESS_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.enums.YunKuaiChongDownlinkCmdEnum.VERIFY_PRICING_ACK;


/**
 * 云快充1.5.0计费模型验证请求
 *
 * @author baigod
 */
@Slf4j
@YunKuaiChongCmd(0x05)
public class YunKuaiChongV150VerifyPricingModelULCmd extends YunKuaiChongUplinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0计费模型验证请求", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();

        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        byte[] pricingModelIdBytes = new byte[2];
        byteBuf.readBytes(pricingModelIdBytes);
        long pricingModelId = BCDUtil.bcdBytesToLong(pricingModelIdBytes);

        // 转发到后端
        VerifyPricingRequest heartBeatRequest = VerifyPricingRequest.builder()
                .pileCode(pileCode)
                .pricingId(pricingModelId)
                .additionalInfo(additionalInfo.toString())
                .build();
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(heartBeatRequest.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
                .verifyPricingRequest(heartBeatRequest)
                .build();
//        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);


        //TODO 调试用   必须登录成功
        YunKuaiChongUplinkMessage requestData = JacksonUtil.fromBytes(uplinkQueueMessage.getRequestData(), YunKuaiChongUplinkMessage.class);

        // 创建ACK消息体7字节桩编号+2字节计费模型编号+1字节验证结果
        ByteBuf verifyPricingAckMsgBody = Unpooled.buffer(10);
        verifyPricingAckMsgBody.writeBytes(pileCodeBytes);
        verifyPricingAckMsgBody.writeBytes(encodePricingId(pricingModelId));
        verifyPricingAckMsgBody.writeByte(SUCCESS_BYTE);

        encodeAndWriteFlush(VERIFY_PRICING_ACK,
            requestData.getSequenceNumber(),
            requestData.getEncryptionFlag(),
            verifyPricingAckMsgBody,
            tcpSession);
    }
}
