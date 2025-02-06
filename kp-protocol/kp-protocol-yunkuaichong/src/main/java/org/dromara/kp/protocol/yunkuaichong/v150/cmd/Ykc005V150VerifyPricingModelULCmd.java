
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;


import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.VerifyPricingRequest;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.SUCCESS_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.VERIFY_PRICING_ACK;


/**
 * 云快充1.5.0计费模型验证请求
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.VERIFY_PRICING_MODEL)
public class Ykc005V150VerifyPricingModelULCmd extends YunKuaiChongUplinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0计费模型验证请求", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();

        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        byte[] pricingModelIdBytes = new byte[2];
        byteBuf.readBytes(pricingModelIdBytes);
        long pricingModelId = BCDUtil.bcdBytesToLong(pricingModelIdBytes);
        additionalInfo.put("计费模型编号", pricingModelId);


        // 转发到后端
        VerifyPricingRequest heartBeatRequest = VerifyPricingRequest.builder()
            .pileCode(pileCode)
            .pricingId(pricingModelId)
            .additionalInfo(additionalInfo.toString())
            .build();
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(heartBeatRequest.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
            .verifyPricingRequest(heartBeatRequest)
            .build();
        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);

        log.info("{} 计费模型验证请求: {}", pileCode, additionalInfo);
    }
}
