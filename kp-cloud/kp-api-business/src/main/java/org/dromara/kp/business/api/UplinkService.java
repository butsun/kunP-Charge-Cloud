package org.dromara.kp.business.api;

import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 17:29
 **/
public interface UplinkService {
    void uplinkCmdProcess(UplinkQueueMessage uplinkMsg);
}
