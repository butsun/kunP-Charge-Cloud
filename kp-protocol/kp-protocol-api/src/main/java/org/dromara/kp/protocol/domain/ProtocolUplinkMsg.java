/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.domain;

import io.netty.buffer.ByteBufUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.SocketAddress;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProtocolUplinkMsg<T> {

    private SocketAddress address;
    private UUID id;
    private T data;
    private int size;


    @Override
    public String toString() {
        if (data instanceof byte[] bytes) {
            return ByteBufUtil.hexDump(bytes);
        } else {
            return data.toString();
        }
    }
}
