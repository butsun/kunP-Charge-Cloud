package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class PeriodProto  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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


