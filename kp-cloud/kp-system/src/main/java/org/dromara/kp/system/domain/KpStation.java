package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 站点管理对象 kp_station
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_station")
public class KpStation extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @OrderBy
    @TableId(value = "id")
    private Long id;

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 站点名称
     */
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
    private Long stationType;

    /**
     * 状态
     */
    private Long stationStatus;

    /**
     * 车位数量
     */
    private Long parkNums;

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
    private Long priceId;

    /**
     * 数据状态:0、正常;1、删除
     */
    @TableLogic
    private Long delFlag;


}
