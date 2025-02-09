package org.dromara.kp.system.domain.vo;

import org.dromara.kp.system.domain.KpConnector;
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
 * 充电枪管理视图对象 kp_connector
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpConnector.class)
public class KpConnectorVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 站点id
     */
    @ExcelProperty(value = "站点id")
    private Long stationId;

    /**
     * 运营商id
     */
    @ExcelProperty(value = "运营商id")
    private Long operatorId;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    private Long equipmentId;


    /**
     * 枪名称
     */
    @ExcelProperty(value = "枪名称")
    private String connectorName;

    /**
     * 枪类型
     */
    @ExcelProperty(value = "枪类型")
    private Long connectorType;

    /**
     * 国标
     */
    @ExcelProperty(value = "国标")
    private Long nationalStandard;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long status;

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
     * 设备号
     */
    @ExcelProperty(value = "设备号")
    private String equipmentNo;

    private Integer netType;

    private String operatorName;

    private String stationName;

    private Integer equipmentType;

    private Integer currentValue;

    private Integer power;

}
