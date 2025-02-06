
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.SetPricingResponse;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.QUERY_PRICING_ACK;

/**
 * 云快充1.5.0 计费模型应答
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.SET_PRICE_ACK)
public class YunKuaiChongV150SetPricingModelAckULCmd extends YunKuaiChongUplinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0计费模型应答", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        // 1.桩编号
        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        // 2.设置结果 0x00:失败 0x01:成功
        boolean isSuccess = (byteBuf.readByte() == 0x01);

        // 从缓存取上个请求的pricingId
        Object pricingId = tcpSession.getRequestCache().asMap().getOrDefault(pileCode + QUERY_PRICING_ACK.getCmd(), null);

        if (pricingId instanceof Long pricingIdL) {
            // 转发到后端
            SetPricingResponse setPricingResponse = SetPricingResponse.builder()
                    .pileCode(pileCode)
                    .success(isSuccess)
                    .pricingId(pricingIdL)
                    .build();
            UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(setPricingResponse.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
                    .setPricingResponse(setPricingResponse)
                    .build();
            tcpSession.getForwarder().sendMessage(uplinkQueueMessage);
        }

    }
}
