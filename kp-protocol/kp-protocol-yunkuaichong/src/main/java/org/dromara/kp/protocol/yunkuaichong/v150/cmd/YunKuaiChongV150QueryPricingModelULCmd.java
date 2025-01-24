
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.QueryPricingRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.QUERY_PRICING_ACK;

/**
 * 云快充1.5.0充电桩计费模型请求
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(0x09)
public class YunKuaiChongV150QueryPricingModelULCmd extends YunKuaiChongUplinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0充电桩计费模型请求", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();

        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        // 转发到后端
        QueryPricingRequest queryPricingRequest = QueryPricingRequest.builder()
                .pileCode(pileCode)
                .additionalInfo(additionalInfo.toString())
                .build();
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(queryPricingRequest.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
                .queryPricingRequest(queryPricingRequest)
                .build();
//        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);



        //TODO 调试用   必须登录成功

        // 创建ACK消息体7字节桩编号+2字节计费模型编号+4x4x2字节尖峰平谷电价和服务费+1字节计损比例+48字节时段标识
        ByteBuf queryPricingAckMsgBody = Unpooled.buffer(90);
        queryPricingAckMsgBody.writeBytes(pileCodeBytes);
        queryPricingAckMsgBody.writeBytes(encodePricingId(1));

        // 4字节电价+4字节服务费
        BigDecimal accurate = new BigDecimal(1000);
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(0).multiply(accurate).intValue());

        // 计损比例
        queryPricingAckMsgBody.writeByte(0);

        // 48段半小时
        byte[] bytes = new byte[48];
        LocalTime currentTime = LocalTime.MIDNIGHT;
        for (int i = 0; i < 48; i++) {
            bytes[i] = 0x00;
            currentTime = currentTime.plusMinutes(30); // 每次时间增加30分钟
        }
        queryPricingAckMsgBody.writeBytes(bytes);

        encodeAndWriteFlush(QUERY_PRICING_ACK,
            queryPricingAckMsgBody,
            tcpSession);
    }
}
