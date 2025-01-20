package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
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

    /**
     * 自增id
     */
    private Long id;

    /**
     * 充电站ID
     */
    @NotBlank(message = "充电站ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long stationId;

    /**
     * 价格模版编号
     */
    private Long priceCode;

    /**
     * 时段起始时间点 6位 HHmmss
     */
    @NotNull(message = "时段起始时间点 6位 HHmmss不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 价格类型:0、尖;1、峰;2、平;3、谷;
     */
    @NotNull(message = "价格类型:0、尖;1、峰;2、平;3、谷;不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long priceType;

    /**
     * 电价:XXXX.XXXX
     */
    @NotNull(message = "电价:XXXX.XXXX不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long elecPrice;

    /**
     * 服务费单价:XXXX.XXXX
     */
    @NotNull(message = "服务费单价:XXXX.XXXX不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long servicePrice;

    /**
     * 备注
     */
    private String remark;


}
