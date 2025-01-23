package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class PricingModelProto {
    private PricingModelType type;
    private PricingModelRule rule;
    private String standardElec;
    private String standardServ;
    private Map<Integer, FlagPriceProto> flagPrices;
    private List<PeriodProto> periods;


    @Getter
    public enum PricingModelType {
        CHARGE,
        DISCHARGE
    }

    @Getter
    public enum PricingModelRule {
        STANDARD,
        SPLIT_TIME
    }
}
