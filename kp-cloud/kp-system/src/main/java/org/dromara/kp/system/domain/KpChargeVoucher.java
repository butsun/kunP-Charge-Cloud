package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 充电凭证管理对象 kp_charge_voucher
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_charge_voucher")
public class KpChargeVoucher extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @OrderBy
    @TableId(value = "id")
    private Long id;

    /**
     * 凭证编号
     */
    private String voucherNumber;

    /**
     * 归属运营商
     */
    private Long operatorId;

    /**
     * 凭证类型
     */
    private Long voucherType;

    /**
     * 归属账户
     */
    private Long accountId;

    /**
     * 禁用
     */
    private Long disableFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据状态:0、正常;1、删除
     */
    private Long delFlag;


}
