package org.dromara.kp.protocol.forwarder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.kp.business.api.UplinkService;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 14:18
 **/
@Service
public class DubboForwarder extends Forwarder{

    @DubboReference
    private UplinkService uplinkService;

    @Override
    public void sendMessage(UplinkQueueMessage msg, BiConsumer<Boolean, ObjectNode> consumer) {
        uplinkService.uplinkCmdProcess(msg);
    }

    @Override
    public void sendMessage(UplinkQueueMessage msg) {
        uplinkService.uplinkCmdProcess(msg);
    }
}
