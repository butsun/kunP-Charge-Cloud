package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
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
