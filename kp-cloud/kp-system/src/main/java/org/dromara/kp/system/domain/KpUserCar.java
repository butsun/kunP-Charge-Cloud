package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 车辆管理对象 kp_user_car
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_user_car")
public class KpUserCar extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 车辆ID
     */
    @OrderBy
    @TableId(value = "id")
    private Long id;

    /**
     * 归属账户
     */
    private Long accountId;

    /**
     * 车牌号
     */
    private String plateNo;

    /**
     * vin码
     */
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
     * 行驶证图片json串
     */
    private String licenseImgs;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据状态:0、正常;1、删除
     */
    @TableLogic
    private Long delFlag;


}
