/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.yunkuaichong.domain.model;

import lombok.*;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PricingModelProto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class PricingModel  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    // 计数器，供充电桩协议使用
    private int sequenceNumber;

    private String pileCode;

    private PricingModelProto.PricingModelType type;

    private PricingModelProto.PricingModelRule rule;

    /**
     * 标准电价（单位元）
     */
    private BigDecimal standardElec;

    /**
     * 标准服务费（单位元）
     */
    private BigDecimal standardServ;

    /**
     * 分时电价
     */
    private Map<PeriodProto.PricingModelFlag, FlagPrice> flagPriceList;

    /**
     * 分时时段
     */
    private List<Period> periodsList;

    @Setter
    @Getter
    public static class Period  implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private int sn;

        // 起始时间
        private LocalTime begin;

        // 结束时间
        private LocalTime end;

        // 尖峰平谷标识
        private PeriodProto.PricingModelFlag flag;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlagPrice  implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        // 分时电价，单位元
        private BigDecimal elec;

        // 分时服务费，单位元
        private BigDecimal serv;
    }

}
