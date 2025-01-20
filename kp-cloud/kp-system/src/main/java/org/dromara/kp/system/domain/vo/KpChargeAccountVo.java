package org.dromara.kp.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.kp.system.domain.KpChargeAccount;
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
 * 充电账户视图对象 kp_charge_account
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpChargeAccount.class)
public class KpChargeAccountVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String mobile;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    private String nickName;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_user_sex")
    private Long sex;

    /**
     * 账户类型
     */
    @ExcelProperty(value = "账户类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_accout_type")
    private Long accoutType;

    /**
     * 禁用
     */
    @ExcelProperty(value = "禁用", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_disable_flag")
    private Long disableFlag;

    /**
     * 注册时间
     */
    @ExcelProperty(value = "注册时间")
    private Date registerTime;

    /**
     * 最近一次访问时间
     */
    @ExcelProperty(value = "最近一次访问时间")
    private Date lastVisitTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
