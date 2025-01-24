
package org.dromara.kp.protocol.yunkuaichong;

import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.dromara.kp.protocol.listener.tcp.TcpSession;

/**
 * @author but
 */
@Slf4j
public abstract class YunKuaiChongUplinkCmdExe extends AbstractYunKuaiChongCmdExe {

    public abstract void execute(TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage, ProtocolContext ctx);

    protected static UplinkQueueMessage.UplinkQueueMessageBuilder uplinkMessageBuilder(String messageKey, TcpSession tcpSession, YunKuaiChongUplinkMessage yunKuaiChongUplinkMessage) {
        return UplinkQueueMessage.builder()
                .messageIdMSB(yunKuaiChongUplinkMessage.getId().getMostSignificantBits())
                .messageIdLSB(yunKuaiChongUplinkMessage.getId().getLeastSignificantBits())
                .sessionIdMSB(tcpSession.getId().getMostSignificantBits())
                .sessionIdLSB(tcpSession.getId().getLeastSignificantBits())
                .requestData(JacksonUtil.writeValueAsBytes(yunKuaiChongUplinkMessage))
                .messageKey(messageKey)
                .protocolName(tcpSession.getProtocolName());
    }

}
