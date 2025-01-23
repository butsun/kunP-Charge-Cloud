package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChargingProgressProto {
    private long ts;
    private String pileCode;
    private String gunCode;
    private String tradeNo;
    private String outputVoltage;
    private String outputCurrent;
    private int soc;
    private int totalChargingDurationMin;
    private String totalChargingEnergyKWh;
    private String totalChargingCostYuan;
    private String additionalInfo;
}
