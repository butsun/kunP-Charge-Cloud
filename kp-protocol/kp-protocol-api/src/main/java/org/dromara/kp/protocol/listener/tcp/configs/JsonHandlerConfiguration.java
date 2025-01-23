/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.listener.tcp.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.dromara.kp.protocol.cfg.enums.TcpHandlerType;

import static org.dromara.kp.protocol.cfg.enums.TcpHandlerType.JSON;


@Data
@ToString
@EqualsAndHashCode
public class JsonHandlerConfiguration implements HandlerConfiguration {
    public TcpHandlerType getType() {
        return JSON;
    }

}
