package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PeriodProto {
    private int sn;
    private String begin;
    private String end;
    private PricingModelFlag flag;


    @Getter
    public enum PricingModelFlag {
        TOP,
        PEAK,
        FLAT,
        VALLEY,
        DEEP
    }
}


