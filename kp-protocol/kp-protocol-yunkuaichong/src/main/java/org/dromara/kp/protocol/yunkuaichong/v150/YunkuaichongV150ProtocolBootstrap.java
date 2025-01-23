/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.yunkuaichong.v150;

import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.annotation.ProtocolComponent;
import org.dromara.kp.protocol.ProtocolBootstrap;
import org.dromara.kp.protocol.ProtocolMessageProcessor;
import org.dromara.kp.protocol.yunkuaichong.YunKuaiChongProtocolMessageProcessor;

/**
 * @author baigod
 */

@ProtocolComponent(YunkuaichongV150ProtocolBootstrap.PROTOCOL_NAME)
@Slf4j
public class YunkuaichongV150ProtocolBootstrap extends ProtocolBootstrap {

    public static final String PROTOCOL_NAME = "yunkuaichongV150";

    @Override
    protected String getProtocolName() {
        return PROTOCOL_NAME;
    }

    @Override
    protected void _init() {
        // do nothing
    }

    @Override
    protected void _destroy() {
        // do nothing
    }

    @Override
    protected ProtocolMessageProcessor messageProcessor() {
        return new YunKuaiChongProtocolMessageProcessor(forwarder, protocolContext);
    }


}
