package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpChargeAccount;
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
 * 充电账户业务对象 kp_charge_account
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpChargeAccount.class, reverseConvertGenerate = false)
public class KpChargeAccountBo extends BaseEntity {

    private Long id;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String mobile;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nickName;

    /**
     * 性别
     */
    private Long sex;

    /**
     * 账户类型
     */
    private Long accoutType;

    /**
     * 禁用
     */
    private Long disableFlag;

    /**
     * 备注
     */
    private String remark;


}
