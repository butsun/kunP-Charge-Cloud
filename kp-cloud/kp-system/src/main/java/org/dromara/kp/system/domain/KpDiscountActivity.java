package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 充电优惠管理对象 kp_discount_activity
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_discount_activity")
public class KpDiscountActivity extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 活动简称
     */
    private String activityName;

    /**
     * 运营商ID（组织机构代码）
     */
    private Long operatorId;

    /**
     * 站点id
     */
    private Long stationId;

    /**
     * 服务费折扣
     */
    private BigDecimal disService;

    /**
     * 充电费折扣
     */
    private BigDecimal disElectricity;

    /**
     * 禁用
     */
    private Integer disableFlag;

    /**
     * 活动类型
     */
    private Integer activityType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记
     */
    @TableLogic
    private Long delFlag;


}
