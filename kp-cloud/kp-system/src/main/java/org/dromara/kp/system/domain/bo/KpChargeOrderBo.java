package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpChargeOrder;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 充电订单管理业务对象 kp_charge_order
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpChargeOrder.class, reverseConvertGenerate = false)
public class KpChargeOrderBo extends BaseEntity {


    private Long id;

    /**
     * 订单号
     */
    private String startChargeSeq;

    /**
     * 运营商ID
     */
    private Long operatorId;

    /**
     * 订单状态
     */
    private Integer startChargeSeqStat;

    /**
     * 站点id
     */
    private Long stationId;

    /**
     * 开始充电时间
     */
    private Date startTime;


}
