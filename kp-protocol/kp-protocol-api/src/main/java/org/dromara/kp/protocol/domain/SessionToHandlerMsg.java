/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.kp.protocol.domain.dto.DownlinkRequestMessage;

/**
 * @author baigod
 */
@Data
@AllArgsConstructor
public class SessionToHandlerMsg{

    private DownlinkRequestMessage downlinkMsg;
    private ProtocolSession session;


}
