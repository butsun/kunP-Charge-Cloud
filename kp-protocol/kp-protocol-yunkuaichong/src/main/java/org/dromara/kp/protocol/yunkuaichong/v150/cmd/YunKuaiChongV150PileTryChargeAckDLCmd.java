package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.LoginResponse;

import java.util.Arrays;
import java.util.Objects;

import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.FAILURE_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.SUCCESS_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.LOGIN_ACK;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.PILE_TRY_CHARGE_ACK;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:59
 **/
@Slf4j
@YunKuaiChongCmd(downCmd = PILE_TRY_CHARGE_ACK)
public class YunKuaiChongV150PileTryChargeAckDLCmd extends YunKuaiChongDownlinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0运营平台确认启动充电", tcpSession);
        // 创建ACK消息体 16字节流水号+7字节桩编号+1字节枪号+8字节逻辑卡号+4字节账户余额+1字节鉴权成功标记+1字节失败原因
        ByteBuf tryChargeAck = Unpooled.buffer(38);

//        tryChargeAck.writeBytes();
//        tryChargeAck.writeBytes();
//        tryChargeAck.writeByte();
//
//        tryChargeAck.writeBytes();
//        tryChargeAck.writeBytes();
//        tryChargeAck.writeByte();
//        tryChargeAck.writeByte();


//        encodeAndWriteFlush(PILE_TRY_CHARGE_ACK,
//            requestData.getSequenceNumber(),
//            requestData.getEncryptionFlag(),
//            tryChargeAck,
//            tcpSession);

    }
}
