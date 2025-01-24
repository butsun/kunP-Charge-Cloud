
package org.dromara.kp.protocol.yunkuaichong;


import org.dromara.kp.protocol.ProtocolContext;
import org.dromara.kp.protocol.listener.tcp.TcpSession;

/**
 * @author but
 */
public abstract class YunKuaiChongDownlinkCmdExe extends AbstractYunKuaiChongCmdExe {

    public abstract void execute(TcpSession tcpSession, YunKuaiChongDwonlinkMessage yunKuaiChongDwonlinkMessage, ProtocolContext ctx);

}
