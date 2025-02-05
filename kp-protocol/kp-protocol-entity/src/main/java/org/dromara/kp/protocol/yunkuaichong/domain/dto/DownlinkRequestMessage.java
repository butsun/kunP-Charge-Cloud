package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:37
 **/
@Data
@AllArgsConstructor
@Builder
public class DownlinkRequestMessage {
    private long messageIdMSB;
    private long messageIdLSB;
    private long sessionIdMSB;
    private long sessionIdLSB;
    private String protocolName;
    private String pileCode;
    private Long requestIdMSB;
    private Long requestIdLSB;
    private byte[] requestData;
    private String downlinkCmd;
    private LoginResponse loginResponse;
    private VerifyPricingResponse verifyPricingResponse;
    private QueryPricingResponse queryPricingResponse;
    private SetPricingRequest setPricingRequest;
    private RemoteStartChargingRequest remoteStartChargingRequest;
    private RemoteStopChargingRequest remoteStopChargingRequest;
    private TransactionRecordAck transactionRecordAck;
}
