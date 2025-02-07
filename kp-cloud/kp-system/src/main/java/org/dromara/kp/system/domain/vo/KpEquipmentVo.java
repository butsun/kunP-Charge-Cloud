package org.dromara.kp.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.kp.system.domain.KpEquipment;
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
 * 充电设备管理视图对象 kp_equipment
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpEquipment.class)
public class KpEquipmentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 设备编号
     */
    private String equipmentNo;

    /**
     * 站点id
     */
    @ExcelProperty(value = "站点id")
    private Long stationId;

    @ExcelProperty(value = "站点名称")
    private String stationName;


    /**
     * 运营商id
     */
    @ExcelProperty(value = "运营商id")
    private Long operatorId;


    @ExcelProperty(value = "运营商名称")
    private String operatorName;

    /**
     * 设备生产商名称
     */
    @ExcelProperty(value = "设备生产商名称")
    private String manufacturerName;

    /**
     * 设备型号
     */
    @ExcelProperty(value = "设备型号")
    private String equipmentModel;

    /**
     * 额定电压上限(单位:V)
     */
    @ExcelProperty(value = "额定电压上限(单位:V)")
    private Integer voltageUpperLimits;

    /**
     * 额定电压下限(单位:V)
     */
    @ExcelProperty(value = "额定电压下限(单位:V)")
    private Integer voltageLowerLimits;

    /**
     * 额定电流
     */
    @ExcelProperty(value = "额定电流")
    private Integer currentValue;

    /**
     * 额定功率
     */
    @ExcelProperty(value = "额定功率")
    private Integer power;

    /**
     * 设备类型
     */
    @ExcelProperty(value = "设备类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_equipment_type")
    private Integer equipmentType;

    /**
     * 工作状态
     */
    @ExcelProperty(value = "工作状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_is_working")
    private Long isWorking;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称")
    private String equipmentName;

    /**
     * 联网类型
     */
    @ExcelProperty(value = "联网类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_net_type")
    private Integer netType;

    /**
     * 最近上线时间
     */
    @ExcelProperty(value = "最近上线时间")
    private Date onlineTm;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 枪数量
     */
    @ExcelProperty(value = "枪数量")
    private Integer gunSum;
}
