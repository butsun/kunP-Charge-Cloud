package org.dromara.kp.system.domain;

import jakarta.validation.constraints.NotBlank;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 充电设备管理对象 kp_equipment
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_equipment")
public class KpEquipment extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 设备编号
     */
    private String equipmentNo;

    /**
     * 站点id
     */
    private Long stationId;

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 价格模版
     */
    private Long priceCode;

    /**
     * 设备生产商名称
     */
    private String manufacturerName;

    /**
     * 设备型号
     */
    private String equipmentModel;

    /**
     * 额定电压上限(单位:V)
     */
    private Long voltageUpperLimits;

    /**
     * 额定电压下限(单位:V)
     */
    private Long voltageLowerLimits;

    /**
     * 额定电流
     */
    private Long currentValue;

    /**
     * 额定功率
     */
    private Long power;

    /**
     * 设备类型
     */
    private Long equipmentType;

    /**
     * 枪数量
     */
    private Integer gunSum;

    /**
     * 车位号
     */
    private String parkNo;

    /**
     * 充电桩最大允许输出功率 30%-100% 1Bin表示1%
     */
    private Long maxPower;

    /**
     * 工作状态
     */
    private Long isWorking;

    /**
     * 最近对时时间
     */
    private Date syncTm;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 联网类型
     */
    private Integer netType;

    /**
     * 最近上线时间
     */
    private Date onlineTm;

    /**
     * 连接主机IP
     */
    private String servIp;

    /**
     * 删除标记
     */
    @TableLogic
    private Long delFlag;


}
