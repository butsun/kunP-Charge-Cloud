
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.FlagPriceProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PricingModelProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.SetPricingRequest;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto.PricingModelFlag.*;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.QUERY_PRICING_ACK;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.SET_PRICING;

/**
 * 云快充1.5.0 计费模型设置
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(downCmd = SET_PRICING)
public class Ykc058V150SetPricingModelDLCmd extends YunKuaiChongDownlinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0计费模型设置 {}", tcpSession,yunKuaiChongDwonlinkMessage.getMsg().getSetPricingRequest());

        if (yunKuaiChongDwonlinkMessage.getMsg().getSetPricingRequest() == null) {
            return;
        }

        SetPricingRequest setPricingRequest = yunKuaiChongDwonlinkMessage.getMsg().getSetPricingRequest();

        long pricingId = setPricingRequest.getPricingId();
        String pileCode = setPricingRequest.getPileCode();
        PricingModelProto pricingModel = setPricingRequest.getPricingModel();
        Map<Integer, FlagPriceProto> flagPriceMap = pricingModel.getFlagPrices();
        List<PeriodProto> periodList = pricingModel.getPeriods();

        // 反转取出桩编号字节数组
        byte[] pileCodeBytes = encodePileCode(pileCode);

        // 创建ACK消息体7字节桩编号+2字节计费模型编号+4x4x2字节尖峰平谷电价和服务费+1字节计损比例+48字节时段标识
        ByteBuf setPricingAckMsgBody = Unpooled.buffer(90);
        setPricingAckMsgBody.writeBytes(pileCodeBytes);
        setPricingAckMsgBody.writeBytes(encodePricingId(pricingId));

        // 4字节电价+4字节服务费
        BigDecimal accurate = new BigDecimal(100000);
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(TOP.ordinal()).getElec()).multiply(accurate).intValue());
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(TOP.ordinal()).getServ()).multiply(accurate).intValue());
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(PEAK.ordinal()).getElec()).multiply(accurate).intValue());
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(PEAK.ordinal()).getServ()).multiply(accurate).intValue());
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(FLAT.ordinal()).getElec()).multiply(accurate).intValue());
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(FLAT.ordinal()).getServ()).multiply(accurate).intValue());
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(VALLEY.ordinal()).getElec()).multiply(accurate).intValue());
        setPricingAckMsgBody.writeIntLE(new BigDecimal(flagPriceMap.get(VALLEY.ordinal()).getServ()).multiply(accurate).intValue());

        // 计损比例
        setPricingAckMsgBody.writeByte(0);

        // 48段半小时
        byte[] bytes = new byte[48];
        LocalTime currentTime = LocalTime.MIDNIGHT;
        for (int i = 0; i < 48; i++) {
            bytes[i] = getFlagForCurrentTime(periodList, currentTime);
            currentTime = currentTime.plusMinutes(30); // 每次时间增加30分钟
        }
        setPricingAckMsgBody.writeBytes(bytes);

        // 放进缓存后再下发
        tcpSession.getRequestCache().put(pileCode + QUERY_PRICING_ACK.getCmd(), Long.valueOf(pricingId));

        encodeAndWriteFlush(SET_PRICING,
                setPricingAckMsgBody,
                tcpSession);
    }
}
