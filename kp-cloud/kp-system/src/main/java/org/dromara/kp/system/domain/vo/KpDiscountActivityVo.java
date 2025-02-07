package org.dromara.kp.system.domain.vo;

import org.dromara.kp.system.domain.KpDiscountActivity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 充电优惠管理视图对象 kp_discount_activity
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpDiscountActivity.class)
public class KpDiscountActivityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 活动简称
     */
    @ExcelProperty(value = "活动简称")
    private String activityName;

    /**
     * 运营商ID（组织机构代码）
     */
    @ExcelProperty(value = "运营商ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "组=织机构代码")
    private String operatorId;

    /**
     * 站点id
     */
    @ExcelProperty(value = "站点id")
    private String stationId;

    /**
     * 服务费折扣
     */
    @ExcelProperty(value = "服务费折扣")
    private BigDecimal disService;

    /**
     * 充电费折扣
     */
    @ExcelProperty(value = "充电费折扣")
    private BigDecimal disElectricity;

    /**
     * 禁用
     */
    @ExcelProperty(value = "禁用", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_disable_flag")
    private Long disableFlag;

    /**
     * 活动类型
     */
    @ExcelProperty(value = "活动类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_activity_type")
    private Long activityType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
