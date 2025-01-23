package org.dromara.kp.system.domain.vo;

import org.dromara.kp.system.domain.KpUserCar;
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
 * 车辆管理视图对象 kp_user_car
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpUserCar.class)
public class KpUserCarVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 车辆ID
     */
    @ExcelProperty(value = "车辆ID")
    private Long id;

    /**
     * 归属账户
     */
    @ExcelProperty(value = "归属账户")
    private Long accountId;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String plateNo;

    /**
     * vin码
     */
    @ExcelProperty(value = "vin码")
    private String carVin;

    /**
     * 品牌
     */
    @ExcelProperty(value = "品牌")
    private String carModel;

    /**
     * 使用性质: 默认 0:运营;、1:非运营;
     */
    @ExcelProperty(value = "使用性质: 默认 0:运营;、1:非运营;", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_use_character")
    private Long useCharacter;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
