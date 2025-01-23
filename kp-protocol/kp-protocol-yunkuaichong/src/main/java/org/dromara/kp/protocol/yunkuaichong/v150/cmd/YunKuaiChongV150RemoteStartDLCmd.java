/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.yunkuaichong.v150.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.kp.infrastructure.util.codec.BCDUtil;
import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.domain.dto.RemoteStartChargingRequest;
import org.dromara.kp.protocol.listener.tcp.TcpSession;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDownlinkCmdExe;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongDwonlinkMessage;
import org.dromara.kp.protocol.yunkuaichong.annotation.YunKuaiChongCmd;

import java.math.BigDecimal;
import java.util.Objects;

import static org.dromara.kp.protocol.yunkuaichong.enums.YunKuaiChongDownlinkCmdEnum.REMOTE_START_CHARGING;

/**
 * 云快充1.5.0 运营平台远程控制启机
 *
 * @author baigod
 */
@Slf4j
@YunKuaiChongCmd(0x34)
public class YunKuaiChongV150RemoteStartDLCmd extends YunKuaiChongDownlinkCmdExe {

    @Override
    public void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx) {
        log.info("{} 云快充1.5.0运营平台远程控制启机", tcpSession);

        if (Objects.equals(yunKuaiChongDwonlinkMessage.getMsg().getRemoteStartChargingRequest(), null)) {

            return;
        }

        RemoteStartChargingRequest remoteStartChargingRequest = yunKuaiChongDwonlinkMessage.getMsg().getRemoteStartChargingRequest();
        String pileCode = remoteStartChargingRequest.getPileCode();
        String gunCode = remoteStartChargingRequest.getGunCode();
        String tradeNo = remoteStartChargingRequest.getTradeNo();
        String limitYuan = remoteStartChargingRequest.getLimitYuan();

        byte[] cardNo = encodeCardNo(tradeNo);

        ByteBuf msgBody = Unpooled.buffer(44);
        // 交易流水号
        msgBody.writeBytes(encodeTradeNo(tradeNo));
        // 桩编码
        msgBody.writeBytes(encodePileCode(pileCode));
        // 枪号
        msgBody.writeBytes(encodeGunCode(gunCode));
        // 逻辑卡号 BCD码
        msgBody.writeBytes(cardNo);
        // 物理卡号
        msgBody.writeBytes(cardNo);
        // 账户余额
        msgBody.writeIntLE(new BigDecimal(limitYuan).intValue());

        encodeAndWriteFlush(REMOTE_START_CHARGING,
                msgBody,
                tcpSession);
    }

    /**
     * 用交易流水号做卡号
     */
    private static byte[] encodeCardNo(String tradeNo) {
        tradeNo = StringUtils.right(tradeNo, 16);
        tradeNo = StringUtils.leftPad(tradeNo, 16, '0');
        return BCDUtil.toBytes(tradeNo);
    }
}
