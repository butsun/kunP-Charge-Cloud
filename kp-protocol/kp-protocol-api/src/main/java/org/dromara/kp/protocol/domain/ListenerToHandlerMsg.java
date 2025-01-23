/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;


@Data
@AllArgsConstructor
public class ListenerToHandlerMsg {
    private  UUID id;
    private  byte[] msg;
    private  ProtocolSession session;
}
