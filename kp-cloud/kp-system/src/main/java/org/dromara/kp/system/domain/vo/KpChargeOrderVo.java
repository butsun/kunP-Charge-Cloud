package org.dromara.kp.system.domain.vo;

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
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String startChargeSeq;

    /**
     * 运营商ID
     */
    @ExcelProperty(value = "运营商ID")
    private String operatorId;

    /**
     * 充电流水号
     */
    @ExcelProperty(value = "充电流水号")
    private String tradeNo;

    /**
     * 订单状态
     */
    @ExcelProperty(value = "订单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_start_charge_seq_stat")
    private Long startChargeSeqStat;

    /**
     * 站点id
     */
    @ExcelProperty(value = "站点id")
    private String stationId;

    /**
     * 充电枪号
     */
    @ExcelProperty(value = "充电枪号")
    private String connectorId;

    /**
     * Soc
     */
    @ExcelProperty(value = "Soc")
    private Long soc;

    /**
     * 开始充电时间
     */
    @ExcelProperty(value = "开始充电时间")
    private Date startTime;

    /**
     * 最新采样时间
     */
    @ExcelProperty(value = "最新采样时间")
    private Date endTime;

    /**
     * 充电量
     */
    @ExcelProperty(value = "充电量")
    private Long totalPower;

    /**
     * 电费
     */
    @ExcelProperty(value = "电费")
    private Long elecMoney;

    /**
     * 服务费
     */
    @ExcelProperty(value = "服务费")
    private Long serviceMoney;

    /**
     * 优惠后电费（元）
     */
    @ExcelProperty(value = "优惠后电费", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "元=")
    private Long finalElecMoney;

    /**
     * 优惠后服务费（元）
     */
    @ExcelProperty(value = "优惠后服务费", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "元=")
    private Long finalServiceMoney;

    /**
     * 总金额
     */
    @ExcelProperty(value = "总金额")
    private Long finalTotalMoney;

    /**
     * 故障原因 0无 1此设备不存在 2此设备离线 3设备已停止充电 4-99自定义（参考12.1 充电停止原因代码表）
     */
    @ExcelProperty(value = "故障原因 0无 1此设备不存在 2此设备离线 3设备已停止充电 4-99自定义", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "参=考12.1,充=电停止原因代码表")
    private Long failReason;

    /**
     * vin码
     */
    @ExcelProperty(value = "vin码")
    private String carVin;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String plateNum;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phoneNum;


}
