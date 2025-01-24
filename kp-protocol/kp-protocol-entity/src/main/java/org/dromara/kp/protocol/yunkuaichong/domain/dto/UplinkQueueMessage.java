package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UplinkQueueMessage {
    private long messageIdMSB;
    private long messageIdLSB;
    private long sessionIdMSB;
    private long sessionIdLSB;
    private String messageKey;
    private String protocolName;
    private byte[] requestData;
    private LoginRequest loginRequest;
    private HeartBeatRequest heartBeatRequest;
    private VerifyPricingRequest verifyPricingRequest;
    private QueryPricingRequest queryPricingRequest;
    private GunRunStatusProto gunRunStatusProto;
    private ChargingProgressProto chargingProgressProto;
    private SetPricingResponse setPricingResponse;
    private RemoteStartChargingResponse remoteStartChargingResponse;
    private RemoteStopChargingResponse remoteStopChargingResponse;
    private TransactionRecord transactionRecord;
}
