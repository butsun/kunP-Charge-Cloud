package org.dromara.kp.system.domain.bo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 站点价格模版业务对象 kp_price_template
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpPriceTemplate.class, reverseConvertGenerate = false)
public class KpPriceTemplateBo extends BaseEntity {

    private Long id;

    @NotNull(message = "模版名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String priceName;

    /**
     * 尖电单价:XXXX.XXXX
     */
    @NotNull(message = "尖电单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal topElecPrice;

    /**
     * 尖服务费单价:XXXX.XXXX
     */
    @NotNull(message = "尖服务费单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal topServPrice;

    /**
     * 峰电单价:XXXX.XXXX
     */
    @NotNull(message = "峰电单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal peakElecPrice;
    /**
     * 峰服务费单价:XXXX.XXXX
     */
    @NotNull(message = "峰服务费单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal peakServPrice;

    /**
     * 平电单价:XXXX.XXXX
     */
    @NotNull(message = "平电单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal flatElecPrice;
    /**
     * 平服务费单价:XXXX.XXXX
     */
    @NotNull(message = "平服务费单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal flatServPrice;

    /**
     * 谷电单价:XXXX.XXXX
     */
    @NotNull(message = "谷电单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal valleyElecPrice;
    /**
     * 谷服务费单价:XXXX.XXXX
     */
    @NotNull(message = "谷服务费单价", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal valleyServPrice;

    /**
     * 时段对应类型list  [{start: 00:00, end: 12:00, flag: 1}, {start: 12:00, end: 00:00, flag: 2}]
     */
    @NotNull(message = "时段对应类型", groups = { AddGroup.class, EditGroup.class })
    private String periods;

    /**
     * 备注
     */
    private String remark;


//
//    /**
//     * 自增id
//     */
//    private Long id;
//
//    /**
//     * 价格模版编号
//     */
//    private Long priceCode;
//
//    /**
//     * 时段起始时间点 6位 HHmmss
//     */
//    @NotNull(message = "时段起始时间点 6位 HHmmss不能为空", groups = { AddGroup.class, EditGroup.class })
//    private String startTime;
//
//    /**
//     * 价格类型:0、尖;1、峰;2、平;3、谷;
//     */
//    @NotNull(message = "价格类型:0、尖;1、峰;2、平;3、谷;不能为空", groups = { AddGroup.class, EditGroup.class })
//    private Integer priceType;
//
//    /**
//     * 电价:XXXX.XXXX
//     */
//    @NotNull(message = "电价:XXXX.XXXX不能为空", groups = { AddGroup.class, EditGroup.class })
//    private BigDecimal elecPrice;
//
//    /**
//     * 服务费单价:XXXX.XXXX
//     */
//    @NotNull(message = "服务费单价:XXXX.XXXX不能为空", groups = { AddGroup.class, EditGroup.class })
//    private BigDecimal servicePrice;
//
//    /**
//     * 备注
//     */
//    private String remark;
//

}
