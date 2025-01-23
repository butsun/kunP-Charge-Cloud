package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpDiscountActivity;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 充电优惠管理业务对象 kp_discount_activity
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpDiscountActivity.class, reverseConvertGenerate = false)
public class KpDiscountActivityBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 活动简称
     */
    @NotBlank(message = "活动简称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String activityName;

    /**
     * 运营商ID（组织机构代码）
     */
    @NotBlank(message = "运营商ID（组织机构代码）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long operatorId;

    /**
     * 站点id
     */
    private Long stationId;

    /**
     * 服务费折扣
     */
    @NotNull(message = "服务费折扣不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long disService;

    /**
     * 充电费折扣
     */
    @NotNull(message = "充电费折扣不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long disElectricity;

    /**
     * 禁用
     */
    private Long disableFlag;

    /**
     * 活动类型
     */
    @NotNull(message = "活动类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityType;

    /**
     * 备注
     */
    private String remark;


}
