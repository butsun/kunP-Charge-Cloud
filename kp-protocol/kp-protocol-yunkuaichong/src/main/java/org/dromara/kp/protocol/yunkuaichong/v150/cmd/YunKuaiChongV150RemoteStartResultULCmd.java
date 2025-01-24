
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.infrastructure.util.trace.TracerContextUtil;

import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.RemoteStartChargingResponse;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

/**
 * 云快充1.5.0 远程启动充电命令回复
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.REMOTE_START_RESULT)
public class YunKuaiChongV150RemoteStartResultULCmd extends YunKuaiChongUplinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0远程启动充电命令回复", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        // 从Tracer总获取当前时间
        long ts = TracerContextUtil.getCurrentTracer().getTracerTs();

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();

        // 1.交易流水号
        byte[] tradeNoBytes = new byte[16];
        byteBuf.readBytes(tradeNoBytes);
        String tradeNo = decodeTradeNo(tradeNoBytes);

        // 2.桩编号
        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        // 3.抢号
        byte gunCodeByte = byteBuf.readByte();
        String gunCode = BCDUtil.toString(gunCodeByte);

        // 4.命令执行结果 0x00失败 0x01成功
        boolean isSuccess = (byteBuf.readByte() == 0x01);

        // 5.失败原因 0无 1设备编码不匹配 2枪已在充电 3设备故障 4设备离线 5未插枪
        byte failReasonByte = byteBuf.readByte();
        String failReason = mapFailCode(failReasonByte);

        RemoteStartChargingResponse remoteStartChargingResponse = RemoteStartChargingResponse.builder()
                .ts(ts)
                .pileCode(pileCode)
                .gunCode(gunCode)
                .tradeNo(tradeNo)
                .success(isSuccess)
                .failReason(failReason)
                .additionalInfo(additionalInfo.toString())
                .build();

        // 转发到后端
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(pileCode, tcpSession, yunKuaiChongUplinkMessage)
                .remoteStartChargingResponse(remoteStartChargingResponse)
                .build();

        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);
    }

    public static String mapFailCode(byte failCode) {
        return switch (failCode) {
            case 0x00 -> "无";
            case 0x01 -> "设备编号不匹配";
            case 0x02 -> "枪已在充电";
            case 0x03 -> "设备故障";
            case 0x04 -> "设备离线";
            case 0x05 -> "未插枪";
            case 0x33 -> "充电失败"; // 充电失败或其他相关信息
            case 0x34 -> "待启充"; // 示例，处理收到充电命令
            default -> "未知错误代码";
        };
    }
}
