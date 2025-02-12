package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 运营商管理对象 kp_operator
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_operator")
public class KpOperator extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @OrderBy
    @TableId(value = "id")
    private Long id;

    /**
     * 运营商名称
     */
    private String operatorName;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 删除标记
     */
    private Long delFlag;


}
