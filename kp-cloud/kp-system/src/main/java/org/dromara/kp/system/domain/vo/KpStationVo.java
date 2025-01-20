package org.dromara.kp.system.domain.vo;

import org.dromara.kp.system.domain.KpStation;
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
 * 站点管理视图对象 kp_station
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpStation.class)
public class KpStationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 运营商id
     */
    @ExcelProperty(value = "运营商id")
    private String operatorId;

    /**
     * 站点名称
     */
    @ExcelProperty(value = "站点名称")
    private String stationName;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ExcelProperty(value = "市")
    private String city;

    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String address;

    /**
     * 站点电话
     */
    @ExcelProperty(value = "站点电话")
    private String stationTel;

    /**
     * 服务电话
     */
    @ExcelProperty(value = "服务电话")
    private String serviceTel;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_station_type")
    private Long stationType;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_station_status")
    private Long stationStatus;

    /**
     * 车位数量
     */
    @ExcelProperty(value = "车位数量")
    private Long parkNums;

    /**
     * 营业时间
     */
    @ExcelProperty(value = "营业时间")
    private String busineHours;

    /**
     * 停车费
     */
    @ExcelProperty(value = "停车费")
    private String parkFee;

    /**
     * 备注信息
     */
    @ExcelProperty(value = "备注信息")
    private String remark;

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


}
