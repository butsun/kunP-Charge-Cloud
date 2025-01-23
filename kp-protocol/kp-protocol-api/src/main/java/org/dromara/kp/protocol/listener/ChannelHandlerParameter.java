/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.kp.protocol.ProtocolMessageProcessor;


import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class ChannelHandlerParameter{
    private String protocolName;
    private ProtocolMessageProcessor protocolMessageProcessor;
    private AtomicInteger connectionsGauge;
//    private MessagesStats uplinkMsgStats;
//    private MessagesStats downlinkMsgStats;
//    private DefaultCounter uplinkTrafficCounter;
//    private DefaultCounter downlinkTrafficCounter;
//    private Timer downlinkTimer;
}
