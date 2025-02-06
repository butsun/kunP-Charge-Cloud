package org.dromara.kp.protocol.dubbo;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.kp.business.api.DownlinkService;
import org.dromara.kp.protocol.domain.ProtocolSession;
import org.dromara.kp.protocol.provider.ProtocolSessionRegistryProvider;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.DownlinkRequestMessage;

import java.util.UUID;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 17:33
 **/
@DubboService
@Slf4j
public class DownServiceImpl implements DownlinkService {
    @Resource
    ProtocolSessionRegistryProvider protocolSessionRegistryProvider;

    @Override
    public void downlinkCmdProcess(DownlinkRequestMessage downlinkMsg) {
        log.info("收到dubbo下行请求 {}", downlinkMsg);
        UUID protocolSessionId = new UUID(downlinkMsg.getSessionIdMSB(), downlinkMsg.getSessionIdLSB());
        ProtocolSession protocolSession = protocolSessionRegistryProvider.get(protocolSessionId);
        try {
            if (protocolSession != null) {

                protocolSession.onDownlink(downlinkMsg);
            } else {
                log.info("下发报文时Session未找到 sessionId: {}", protocolSessionId);
            }
        } catch (Exception e) {
            log.warn("下发报文时处理失败 sessionId: {}", protocolSessionId, e);
        }
    }
}
