package org.dromara.kp.business.api;

import org.dromara.kp.protocol.yunkuaichong.domain.dto.DownlinkRequestMessage;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 17:30
 **/
public interface DownlinkService {

    void downlinkCmdProcess(DownlinkRequestMessage downlinkMsg);

}
