package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class ChargingProgressProto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
