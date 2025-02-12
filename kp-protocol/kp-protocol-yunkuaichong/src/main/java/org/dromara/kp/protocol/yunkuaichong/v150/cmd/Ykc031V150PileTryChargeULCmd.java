package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileTryChargeRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
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
public class Ykc031V150PileTryChargeULCmd extends YunKuaiChongUplinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.debug("{} 云快充1.5.0充电桩主动申请启动充电", tcpSession);
        ByteBuf byteBuf = getMessageByteBuf(yunKuaiChongUplinkMessage.getMsgBody());

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();


        //1.桩编号
        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        //2 枪号
        byte gunCodeByte = byteBuf.readByte();
        String gunCode = BCDUtil.toString(gunCodeByte);
        additionalInfo.put("枪号", gunCode);

        //3 启动方式
        byte upType = byteBuf.readByte();
        additionalInfo.put("启动方式 01刷卡  03VIN启动", upType);

        //4 是否需要密码
        additionalInfo.put("是否需要密码 00不需要  01需要", byteBuf.readByte());

        //5 账号或者物理卡号
        long cardNo = byteBuf.readLong();
        additionalInfo.put("账号或物理卡号", cardNo);

        //6 输入密码
        byte[] pwdBytes = new byte[16];
        byteBuf.readBytes(pwdBytes);
        String pwd = new String(pwdBytes);
        additionalInfo.put("输入密码", pwd);


        //7 VIN 码
        byte[] carVINBytes = new byte[17];
        byteBuf.readBytes(carVINBytes);
        String carVin = new String(carVINBytes, StandardCharsets.US_ASCII);
        carVin = StrUtil.reverse(carVin);
        additionalInfo.put("VIN码", carVin);


        // 转发到后端
        PileTryChargeRequest pileTryChargeRequest = PileTryChargeRequest.builder()
            .pileCode(pileCode)
            .chargeType(upType)
            .gunNo(gunCode)
            .cardNo(cardNo)
            .carVin(carVin)
            .build();
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(pileTryChargeRequest.getPileCode(), tcpSession, yunKuaiChongUplinkMessage)
            .pileTryChargeRequest(pileTryChargeRequest)
            .build();
        log.info("{} 充电桩主动申请启动充电: {}", pileCode, pileTryChargeRequest);
        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);


    }

}
