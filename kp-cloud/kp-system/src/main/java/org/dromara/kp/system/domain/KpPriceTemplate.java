package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 站点价格模版对象 kp_price_template
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_price_template")
public class KpPriceTemplate extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 价格模版编号
     */
    private Long priceCode;

    /**
     * 时段起始时间点 6位 HHmmss
     */
    private String startTime;

    /**
     * 价格类型:0、尖;1、峰;2、平;3、谷;
     */
    private Short priceType;

    /**
     * 电价:XXXX.XXXX
     */
    private BigDecimal elecPrice;

    /**
     * 服务费单价:XXXX.XXXX
     */
    private BigDecimal servicePrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 主节点 1
     */
    private Short mainPoint;

    /**
     * 数据状态:0、正常;1、删除
     */
    @TableLogic
    private Long delFlag;


}
