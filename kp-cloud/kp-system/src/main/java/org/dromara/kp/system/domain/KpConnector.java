package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 充电枪管理对象 kp_connector
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_connector")
public class KpConnector extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 站点id
     */
    private Long stationId;

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 设备号
     */
    private Long equipmentId;

    /**
     * 枪号
     */
    private Integer connectorNo;

    /**
     * 枪名称
     */
    private String connectorName;

    /**
     * 枪类型
     */
    private Long connectorType;

    /**
     * 国标
     */
    private Long nationalStandard;

    /**
     * 状态
     */
    private Long status;

    /**
     * 数据状态:0、正常;1、删除
     */
    @TableLogic
    private Long delFlag;


}
