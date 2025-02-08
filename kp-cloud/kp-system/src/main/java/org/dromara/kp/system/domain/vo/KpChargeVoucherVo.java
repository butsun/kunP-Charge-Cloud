package org.dromara.kp.system.domain.vo;

import org.dromara.kp.system.domain.KpChargeVoucher;
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
 * 充电凭证管理视图对象 kp_charge_voucher
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KpChargeVoucher.class)
public class KpChargeVoucherVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 凭证编号
     */
    @ExcelProperty(value = "凭证编号")
    private String voucherNumber;

    /**
     * 归属运营商
     */
    @ExcelProperty(value = "归属运营商")
    private String operatorId;

    /**
     * 凭证类型
     */
    @ExcelProperty(value = "凭证类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_voucher_type")
    private Long voucherType;

    /**
     * 归属账户
     */
    @ExcelProperty(value = "归属账户")
    private Long accountId;

    /**
     * 禁用
     */
    @ExcelProperty(value = "禁用", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kp_disable_flag")
    private Long disableFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
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


    /**
     * 归属运营商
     */
    @ExcelProperty(value = "归属运营商")
    private String operatorName;

    /**
     * 归属账户手机号
     */
    @ExcelProperty(value = "归属账户手机号")
    private String mobile;

    /**
     * 归属账户昵称
     */
    @ExcelProperty(value = "归属账户昵称")
    private String nickName;
}
