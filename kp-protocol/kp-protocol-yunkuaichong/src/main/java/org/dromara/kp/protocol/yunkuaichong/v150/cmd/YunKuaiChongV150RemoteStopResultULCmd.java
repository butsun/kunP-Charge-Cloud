
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.infrastructure.util.trace.TracerContextUtil;

import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.RemoteStopChargingResponse;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

/**
 * 云快充1.5.0 远程停机命令回复
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(upCmd = YunKuaiChongUplinkCmdEnum.REMOTE_STOP_RESULT)
public class YunKuaiChongV150RemoteStopResultULCmd extends YunKuaiChongUplinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0远程停机命令回复", tcpSession);
        ByteBuf byteBuf = Unpooled.copiedBuffer(yunKuaiChongUplinkMessage.getMsgBody());

        // 从Tracer总获取当前时间
        long ts = TracerContextUtil.getCurrentTracer().getTracerTs();

        ObjectNode additionalInfo = JacksonUtil.newObjectNode();

        // 1.桩编号
        byte[] pileCodeBytes = new byte[7];
        byteBuf.readBytes(pileCodeBytes);
        String pileCode = BCDUtil.toString(pileCodeBytes);

        // 2.抢号
        byte gunCodeByte = byteBuf.readByte();
        String gunCode = BCDUtil.toString(gunCodeByte);

        // 3.命令执行结果 0x00失败 0x01成功
        boolean isSuccess = (byteBuf.readByte() == 0x01);

        // 4.失败原因 0无 1设备编码不匹配 2枪已在充电 3设备故障 4设备离线 5未插枪
        byte failReasonByte = byteBuf.readByte();
        String failReason = mapFailCode(failReasonByte);

        RemoteStopChargingResponse remoteStopChargingResponse = RemoteStopChargingResponse.builder()
                .ts(ts)
                .pileCode(pileCode)
                .gunCode(gunCode)
                .success(isSuccess)
                .failReason(failReason)
                .additionalInfo(additionalInfo.toString())
                .build();

        // 转发到后端
        UplinkQueueMessage uplinkQueueMessage = uplinkMessageBuilder(pileCode, tcpSession, yunKuaiChongUplinkMessage)
                .remoteStopChargingResponse(remoteStopChargingResponse)
                .build();

        tcpSession.getForwarder().sendMessage(uplinkQueueMessage);
    }

    public static String mapFailCode(byte failCode) {
        return switch (failCode) {
            case 0x00 -> "无";
            case 0x01 -> "设备编号不匹配";
            case 0x02 -> "枪未处于充电状态";
            case 0x03 -> "其他";
            default -> "未知错误"; // 可以根据需求自定义
        };
    }
}
