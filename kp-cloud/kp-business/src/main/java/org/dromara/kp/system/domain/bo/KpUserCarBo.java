package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpUserCar;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 车辆管理业务对象 kp_user_car
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpUserCar.class, reverseConvertGenerate = false)
public class KpUserCarBo extends BaseEntity {

    /**
     * 车辆ID
     */
    private Long id;

    /**
     * 归属账户
     */
    @NotNull(message = "归属账户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long accountId;

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String plateNo;

    /**
     * vin码
     */
    @NotBlank(message = "vin码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carVin;

    /**
     * 品牌
     */
    private String carModel;

    /**
     * 使用性质: 默认 0:运营;、1:非运营;
     */
    private Long useCharacter;

    /**
     * 备注
     */
    private String remark;


}
