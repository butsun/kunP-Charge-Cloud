package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
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
        TRANSACTION_RECORD, DEEP
    }
}


