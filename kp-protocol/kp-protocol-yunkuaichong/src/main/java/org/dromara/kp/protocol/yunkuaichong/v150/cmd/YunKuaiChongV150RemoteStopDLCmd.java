
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.RemoteStopChargingRequest;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.REMOTE_START_CHARGING;


/**
 * 云快充1.5.0 运营平台远程停机
 *
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(0x36)
public class YunKuaiChongV150RemoteStopDLCmd extends YunKuaiChongDownlinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0运营平台远程停机", tcpSession);

        if (yunKuaiChongDwonlinkMessage.getMsg().getRemoteStopChargingRequest() == null) {
            return;
        }

        RemoteStopChargingRequest remoteStopChargingRequest = yunKuaiChongDwonlinkMessage.getMsg().getRemoteStopChargingRequest();
        String pileCode = remoteStopChargingRequest.getPileCode();
        String gunCode = remoteStopChargingRequest.getGunCode();

        ByteBuf msgBody = Unpooled.buffer(44);
        // 桩编码
        msgBody.writeBytes(encodePileCode(pileCode));
        // 枪号
        msgBody.writeBytes(encodeGunCode(gunCode));

        encodeAndWriteFlush(REMOTE_START_CHARGING,
                msgBody,
                tcpSession);
    }
}
