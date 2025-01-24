package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

import java.nio.charset.StandardCharsets;

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
        byteBuf.readLongLE();

        //6 输入密码
        byte[] pwdBytes = new byte[16];
        byteBuf.readBytes(pwdBytes);
        new String(pwdBytes, StandardCharsets.US_ASCII);

        //7 VIN 码
        byte[] carVINBytes = new byte[17];
        byteBuf.readBytes(carVINBytes);
        new String(carVINBytes, StandardCharsets.US_ASCII);
    }
}
