package org.dromara.kp.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.kp.system.domain.KpChargeOrder;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 充电订单管理视图对象 kp_charge_order
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpChargeOrder.class)
public class KpChargeOrderVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String startChargeSeq;

    /**
     * 运营商ID
     */
    private Long operatorId;

    /**
     * 充电流水号
     */
    @ExcelProperty(value = "流水号")
    private String tradeNo;

    /**
     * 订单状态
     */
    @ExcelProperty(value = "订单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_start_charge_seq_stat")
    private Integer startChargeSeqStat;

    /**
     * 站点id
     */
    private Long stationId;

    /**
     * 充电枪id
     */
    private Long connectorId;

    /**
     * 充电设备id
     */
    private Long equipmentId;

    /**
     * Soc
     */
    private BigDecimal soc;

    /**
     * 开始充电时间
     */
    @ExcelProperty(value = "开始充电时间")
    private Date startTime;

    /**
     * 最新采样时间
     */
    @ExcelProperty(value = "结束充电时间")
    private Date endTime;



    /**
     * 电流
     */
    private BigDecimal gunCurrent;

    /**
     * 电压
     */
    private BigDecimal gunVoltage;

    /**
     * 充电量
     */
    @ExcelProperty(value = "充电量")
    private BigDecimal totalPower;

    /**
     * 电费
     */
    @ExcelProperty(value = "电费")
    private BigDecimal elecMoney;

    /**
     * 服务费
     */
    @ExcelProperty(value = "服务费")
    private BigDecimal serviceMoney;

    /**
     * 优惠后电费（元）
     */
//    @ExcelProperty(value = "优惠后电费", converter = ExcelDictConvert.class)
    private BigDecimal finalElecMoney;

    /**
     * 优惠后服务费（元）
     */
//    @ExcelProperty(value = "优惠后服务费", converter = ExcelDictConvert.class)
    private BigDecimal finalServiceMoney;


    /**
     * 结算金额
     */
    @ExcelProperty(value = "结算金额")
    private BigDecimal finalTotalMoney;

    /**
     * 订单金额
     */
    @ExcelProperty(value = "订单金额")
    private BigDecimal totalMoney;

    /**
     * 故障原因 0无 1此设备不存在 2此设备离线 3设备已停止充电 4-99自定义（参考12.1 充电停止原因代码表）
     */
    private Integer failReason;

    /**
     * vin码
     */
    private String carVin;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 车牌号
     */
    private String plateNum;

    /**
     * 手机号
     */
    private String phoneNum;


    /**
     * 运营商名称
     */
    private String operatorName;

    /**
     * 站点名称
     */
    @ExcelProperty(value = "站点名称")
    private String stationName;

    /**
     * 枪口编号
     */
    private String connectorNo;

    /**
     * 设备编号
     */
    private String equipmentNo;

    /**
     * 停止原因
     */
    @ExcelProperty(value = "停止原因")
    private String stopReason;

    /**
     * 订单归属账户id
     */
    private Long accountId;

    /**
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 启动方式 默认0 未知   01 刷卡  02 账号  03 VIN
     */
    private Integer startType;

}
