package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class UplinkQueueMessage  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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

    /********新增******/
    private PileTryChargeRequest pileTryChargeRequest;
    private SyncTimeResponse syncTimeResponse;

}
