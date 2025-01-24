
package org.dromara.kp.protocol.yunkuaichong;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.DownlinkRequestMessage;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author but
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class YunKuaiChongDwonlinkMessage implements Serializable {
    public static final byte SUCCESS_BYTE = 0x00;
    public static final byte FAILURE_BYTE = 0x01;

    // 消息ID
    private UUID id;

    // 请求ID（如有）
    private UUID requestId;

    // 指令
    private int cmd;

    // 消息体
    private DownlinkRequestMessage msg;

    // 上行消息
    private YunKuaiChongUplinkMessage requestData;

}
