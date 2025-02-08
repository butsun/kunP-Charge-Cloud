package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class TransactionRecord  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String pileCode;
    private int gunCode;
    private String tradeNo;
    private long startTs;
    private long endTs;
    private String topEnergyKWh;
    private String topAmountYuan;
    private String peakEnergyKWh;
    private String peakAmountYuan;
    private String flatEnergyKWh;
    private String flatAmountYuan;
    private String valleyEnergyKWh;
    private String valleyAmountYuan;
    private String deepEnergyKWh;
    private String deepAmountYuan;
    private String totalEnergyKWh;
    private String totalAmountYuan;
    private long tradeTs;
    private String stopReason;
    private String additionalInfo;
}
