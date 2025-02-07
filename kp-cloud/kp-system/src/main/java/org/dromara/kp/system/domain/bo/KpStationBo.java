package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpStation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 站点管理业务对象 kp_station
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpStation.class, reverseConvertGenerate = false)
public class KpStationBo extends BaseEntity {


    private Long id;

    /**
     * 运营商id
     */
    @NotBlank(message = "运营商id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String operatorId;

    /**
     * 站点名称
     */
    @NotBlank(message = "站点名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationName;

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
    @NotBlank(message = "详细地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String address;

    /**
     * 站点电话
     */
    private String stationTel;

    /**
     * 服务电话
     */
    private String serviceTel;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer stationType;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer stationStatus;

    /**
     * 车位数量
     */
    private Integer parkNums;

    /**
     * 营业时间
     */
    private String busineHours;

    /**
     * 停车费
     */
    private String parkFee;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 价格模版code
     */
    private Long priceCode;


}
