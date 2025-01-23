package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpOperator;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 运营商管理业务对象 kp_operator
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpOperator.class, reverseConvertGenerate = false)
public class KpOperatorBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 运营商名称
     */
    @NotBlank(message = "运营商名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String operatorName;

    /**
     * 省
     */
    @NotBlank(message = "省不能为空", groups = { AddGroup.class, EditGroup.class })
    private String province;

    /**
     * 市
     */
    @NotBlank(message = "市不能为空", groups = { AddGroup.class, EditGroup.class })
    private String city;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String address;


}
