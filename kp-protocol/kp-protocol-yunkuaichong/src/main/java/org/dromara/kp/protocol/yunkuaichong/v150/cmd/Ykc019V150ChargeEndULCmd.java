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
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:31
 **/
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.CHARGE_END)
public class Ykc019V150ChargeEndULCmd extends YunKuaiChongUplinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0充电结束", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();
        // 1.交易流水号
        byte[] tradeNoBytes = new byte[16];
        byteBuf.readBytes(tradeNoBytes);
        String tradeNo = decodeTradeNo(tradeNoBytes);
        additionalInfo.put("交易流水号", tradeNo);

        // 2.桩编号
        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);
        additionalInfo.put("桩编号", pileCode);

        // 3.抢号
        byte gunCodeByte = byteBuf.readByte();
        String gunCode = BCDUtil.toString(gunCodeByte);
        additionalInfo.put("抢号", gunCode);

        // 4.BMS 中止荷电状态 SOC
        int bmsStopSoc = byteBuf.readUnsignedByte();
        additionalInfo.put("BMS中止荷电状态SOC", bmsStopSoc);

        //5.BMS 动力蓄电池单体最低电压
        int bmsLowVoltage = byteBuf.readUnsignedShortLE();
        additionalInfo.put("BMS动力蓄电池单体最低电压", bmsLowVoltage);


        //6.BMS 动力蓄电池单体最高电压
        int bmsMaxVoltage =byteBuf.readUnsignedShortLE();
        additionalInfo.put("BMS动力蓄电池单体最高电压", bmsMaxVoltage);

        //7.BMS 动力蓄电池最低温度
        int bmsLowTemp = byteBuf.readByte();
        additionalInfo.put("BMS动力蓄电池最低温度", bmsLowTemp);

        //8.BMS 动力蓄电池最高温度
        int bmsMaxTemp = byteBuf.readByte();
        additionalInfo.put("BMS动力蓄电池最高温度", bmsMaxTemp);

        //9.电桩累计充电时间
        int chargingTime = byteBuf.readUnsignedShortLE();
        additionalInfo.put("电桩累计充电时间", chargingTime);

        //10.电桩输出能量
        int outputEnergy = byteBuf.readUnsignedShortLE();
        additionalInfo.put("电桩输出能量", outputEnergy);

        //11.电桩充电机编号
        int chargingMachineNo = byteBuf.readIntLE();
        additionalInfo.put("电桩充电机编号", chargingMachineNo);

        // TODO 先打印日志，暂不转发
        log.debug("{} 云快充1.5.0充电结束信息解析完成:{}", tcpSession, additionalInfo);
    }
}
