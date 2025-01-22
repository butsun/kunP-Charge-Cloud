package org.dromara.kp.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.kp.system.domain.KpPriceTemplate;
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
 * 站点价格模版视图对象 kp_price_template
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpPriceTemplate.class)
public class KpPriceTemplateVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ExcelProperty(value = "自增id")
    private Long id;


    /**
     * 价格模版编号
     */
    @ExcelProperty(value = "价格模版编号")
    private Long priceCode;

    /**
     * 时段起始时间点 6位 HHmmss
     */
    @ExcelProperty(value = "时段起始时间点 6位 HHmmss")
    private String startTime;

    /**
     * 价格类型:0、尖;1、峰;2、平;3、谷;
     */
    @ExcelProperty(value = "价格类型:0、尖;1、峰;2、平;3、谷;")
    private Short priceType;

    /**
     * 电价:XXXX.XXXX
     */
    @ExcelProperty(value = "电价:XXXX.XXXX")
    private BigDecimal elecPrice;

    /**
     * 服务费单价:XXXX.XXXX
     */
    @ExcelProperty(value = "服务费单价:XXXX.XXXX")
    private BigDecimal servicePrice;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


    @ExcelProperty(value = "修改时间")
    private Date updateTime;



}
