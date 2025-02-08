
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.jackson.JacksonUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.TransactionRecordAck;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongUplinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import java.util.Objects;

import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.FAILURE_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage.SUCCESS_BYTE;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.TRANSACTION_RECORD_ACK;
import static org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum.VERIFY_PRICING_ACK;

/**
 * 云快充1.5.0 交易记录确认
 * @author but
 */
@Slf4j
@YunKuaiChongCmd(downCmd = TRANSACTION_RECORD_ACK)
public class Ykc040V150TransactionRecordAckDLCmd extends YunKuaiChongDownlinkCmdExe {
    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0交易记录确认 {}", tcpSession,yunKuaiChongDwonlinkMessage);

        if (Objects.equals(yunKuaiChongDwonlinkMessage.getMsg().getTransactionRecordAck(),null)) {
            return;
        }

        TransactionRecordAck transactionRecordAck = yunKuaiChongDwonlinkMessage.getMsg().getTransactionRecordAck();

        YunKuaiChongUplinkMessage requestData = JacksonUtil.fromBytes(yunKuaiChongDwonlinkMessage.getMsg().getRequestData(), YunKuaiChongUplinkMessage.class);

        // 创建ACK消息体16字节交易流水号 + 1字节确认结果
        ByteBuf msgBody = Unpooled.buffer(17);
        msgBody.writeBytes(encodeTradeNo(transactionRecordAck.getTradeNo()));
        msgBody.writeByte(transactionRecordAck.isSuccess() ? SUCCESS_BYTE : FAILURE_BYTE);

        encodeAndWriteFlush(TRANSACTION_RECORD_ACK,
                requestData.getSequenceNumber(),
                requestData.getEncryptionFlag(),
                msgBody,
                tcpSession);
    }
}
