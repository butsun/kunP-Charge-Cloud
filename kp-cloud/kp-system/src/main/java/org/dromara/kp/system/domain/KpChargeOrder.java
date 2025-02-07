package org.dromara.kp.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 充电订单管理对象 kp_charge_order
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_charge_order")
public class KpChargeOrder extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 订单号
     */
    private String startChargeSeq;

    /**
     * 运营商ID
     */
    private Long operatorId;

    /**
     * 充电流水号
     */
    private String tradeNo;

    /**
     * 订单状态  ；1启动中 2充电中 3停止中 4已结束 5未知
     */
    private Integer startChargeSeqStat;

    /**
     * 站点id
     */
    private Long stationId;

    /**
     * 关联优惠id
     */
    private Long activityId;

    /**
     * 充电费折扣
     */
    private BigDecimal activittyElec;

    /**
     * 服务费折扣
     */
    private BigDecimal activityService;

    /**
     * 充电枪id
     */
    private Long connectorId;

    /**
     * 充电设备id
     */
    private Long equipmentId;

    /**
     * 电流A
     */
    private BigDecimal currentA;

    /**
     * 电流B
     */
    private BigDecimal currentB;

    /**
     * 电流C
     */
    private BigDecimal currentC;

    /**
     * 电压A
     */
    private BigDecimal voltageA;

    /**
     * 电压B
     */
    private BigDecimal voltageB;

    /**
     * 电压C
     */
    private BigDecimal voltageC;

    /**
     * Soc
     */
    private BigDecimal soc;

    /**
     * 开始充电时间
     */
    private Date startTime;

    /**
     * 最新采样时间
     */
    private Date endTime;

    /**
     * 充电量
     */
    private BigDecimal totalPower;

    /**
     * 电费
     */
    private BigDecimal elecMoney;

    /**
     * 服务费
     */
    private BigDecimal serviceMoney;

    /**
     * 合计费用
     */
    private BigDecimal totalMoney;

    /**
     * 优惠后电费（元）
     */
    private BigDecimal finalElecMoney;

    /**
     * 优惠后服务费（元）
     */
    private BigDecimal finalServiceMoney;

    /**
     * 总金额
     */
    private BigDecimal finalTotalMoney;

    /**
     * 故障原因 0无 1此设备不存在 2此设备离线 3设备已停止充电 4-99自定义（参考12.1 充电停止原因代码表）
     */
    private Integer failReason;

    /**
     * vin码
     */
    private String carVin;

    /**
     * 删除标记
     */
    @TableLogic
    private Long delFlag;

    /**
     * 车牌号
     */
    private String plateNum;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 下单时计价
     */
    private String priceInfo;


    /**
     * 枪口编号
     */
    private Integer connectorNo;

    /**
     * 设备编号
     */
    private String equipmentNo;

}
