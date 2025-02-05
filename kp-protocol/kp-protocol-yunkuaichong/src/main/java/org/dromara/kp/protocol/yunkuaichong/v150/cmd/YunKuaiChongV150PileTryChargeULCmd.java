package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

import java.nio.charset.StandardCharsets;

import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.PILE_TRY_CHARGE_ACK;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:45
 **/
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.PILE_TRY_CHARGE)
public class YunKuaiChongV150PileTryChargeULCmd extends YunKuaiChongUplinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0充电桩主动申请启动充电", tcpSession);
        ByteBuf byteBuf = getMessageByteBuf(yunKuaiChongUplinkMessage.getMsgBody());

        //1.桩编号
        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        //2 枪号
        byte gunCodeByte = byteBuf.readByte();
        String gunCode = BCDUtil.toString(gunCodeByte);

        //3 启动方式
        byteBuf.readByte();

        //4 是否需要密码
        byteBuf.readByte();

        //5 账号或者物理卡号
        long cardNo = byteBuf.readLongLE();

        //6 输入密码
        byte[] pwdBytes = new byte[16];
        byteBuf.readBytes(pwdBytes);
        new String(pwdBytes, StandardCharsets.US_ASCII);

        //7 VIN 码
        byte[] carVINBytes = new byte[17];
        byteBuf.readBytes(carVINBytes);
        new String(carVINBytes, StandardCharsets.US_ASCII);


        //todo 此处必须成功
        loginAck(tcpSession, pileCode, gunCode, cardNo, yunKuaiChongUplinkMessage);


    }


    private void loginAck(TcpSession tcpSession, String pileCodeBytes, String gunCode, Long cardNo, YunKuaiChongUplinkMessage requestData) {
        // 创建ACK消息体16字节流水号+7字节桩编号+1字节枪号+8字节卡号+4字节账户余额+1字节鉴权结果+1字节失败原因
        ByteBuf tryChargeAckMsgBody = Unpooled.buffer(38);
        byte[] tradeNo = encodeTradeNo("32010600019236012001061803423060");

        tryChargeAckMsgBody.writeBytes(tradeNo);
        tryChargeAckMsgBody.writeBytes(encodePileCode(pileCodeBytes));
        tryChargeAckMsgBody.writeBytes(encodeGunCode(gunCode));
        tryChargeAckMsgBody.writeLongLE(cardNo);
        tryChargeAckMsgBody.writeIntLE(0);
        tryChargeAckMsgBody.writeByte(0x01);
        tryChargeAckMsgBody.writeByte(0);

        encodeAndWriteFlush(PILE_TRY_CHARGE_ACK,
            requestData.getSequenceNumber(),
            requestData.getEncryptionFlag(),
            tryChargeAckMsgBody,
            tcpSession);
    }
}
