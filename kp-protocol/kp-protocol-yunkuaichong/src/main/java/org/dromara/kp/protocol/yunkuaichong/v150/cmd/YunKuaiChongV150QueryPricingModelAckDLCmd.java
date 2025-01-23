/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.domain.dto.FlagPriceProto;
import org.dromara.kp.protocol.domain.dto.PeriodProto;
import org.dromara.kp.protocol.domain.dto.PricingModelProto;
import org.dromara.kp.protocol.domain.dto.QueryPricingResponse;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.dromara.kp.protocol.yunkuaichong.enums.YunKuaiChongDownlinkCmdEnum.QUERY_PRICING_ACK;
import static org.dromara.kp.protocol.domain.dto.PeriodProto.PricingModelFlag.*;


/**
 * 计费模型请求应答
 *
 * @author baigod
 */
@Slf4j
@YunKuaiChongCmd(0x0A)
public class YunKuaiChongV150QueryPricingModelAckDLCmd extends YunKuaiChongDownlinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0计费模型请求应答", tcpSession);

        if (Objects.equals(yunKuaiChongDwonlinkMessage.getMsg().getQueryPricingResponse(),null)) {
            return;
        }

        QueryPricingResponse queryPricingResponse = yunKuaiChongDwonlinkMessage.getMsg().getQueryPricingResponse();

        long pricingId = queryPricingResponse.getPricingId();
        String pileCode = queryPricingResponse.getPileCode();
        PricingModelProto pricingModel = queryPricingResponse.getPricingModel();
        Map<Integer, FlagPriceProto> flagPriceMap = pricingModel.getFlagPrices();
        List<PeriodProto> periodList = pricingModel.getPeriods();

        // 从上行报文中取出桩编号字节数组
        byte[] pileCodeBytes = encodePileCode(pileCode);

        // 创建ACK消息体7字节桩编号+2字节计费模型编号+4x4x2字节尖峰平谷电价和服务费+1字节计损比例+48字节时段标识
        ByteBuf queryPricingAckMsgBody = Unpooled.buffer(90);
        queryPricingAckMsgBody.writeBytes(pileCodeBytes);
        queryPricingAckMsgBody.writeBytes(encodePricingId(pricingId));

        // 4字节电价+4字节服务费
        BigDecimal accurate = new BigDecimal(1000);
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(TOP.ordinal()).getElec()).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(TOP.ordinal()).getServ()).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(PEAK.ordinal()).getElec()).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(PEAK.ordinal()).getServ()).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(FLAT.ordinal()).getElec()).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(FLAT.ordinal()).getServ()).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(VALLEY.ordinal()).getElec()).multiply(accurate).intValue());
        queryPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(VALLEY.ordinal()).getServ()).multiply(accurate).intValue());

        // 计损比例
        queryPricingAckMsgBody.writeByte(0);

        // 48段半小时
        byte[] bytes = new byte[48];
        LocalTime currentTime = LocalTime.MIDNIGHT;
        for (int i = 0; i < 48; i++) {
            bytes[i] = getFlagForCurrentTime(periodList, currentTime);
            currentTime = currentTime.plusMinutes(30); // 每次时间增加30分钟
        }
        queryPricingAckMsgBody.writeBytes(bytes);

        encodeAndWriteFlush(QUERY_PRICING_ACK,
                queryPricingAckMsgBody,
                tcpSession);

    }
}
