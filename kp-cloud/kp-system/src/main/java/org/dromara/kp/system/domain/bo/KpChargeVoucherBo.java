package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpChargeVoucher;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 充电凭证管理业务对象 kp_charge_voucher
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpChargeVoucher.class, reverseConvertGenerate = false)
public class KpChargeVoucherBo extends BaseEntity {


    private Long id;

    /**
     * 凭证编号
     */
    @NotBlank(message = "凭证编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String voucherNumber;

    /**
     * 归属运营商
     */
    @NotBlank(message = "归属运营商不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long operatorId;

    /**
     * 凭证类型
     */
    @NotNull(message = "凭证类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long voucherType;

    /**
     * 归属账户
     */
    @NotNull(message = "归属账户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long accountId;

    /**
     * 禁用
     */
    private Long disableFlag;

    /**
     * 备注
     */
    private String remark;


}
