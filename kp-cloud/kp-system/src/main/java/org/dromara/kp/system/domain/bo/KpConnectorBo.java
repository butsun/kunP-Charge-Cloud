package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpConnector;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 充电枪管理业务对象 kp_connector
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpConnector.class, reverseConvertGenerate = false)
public class KpConnectorBo extends BaseEntity {


    private Long id;

    /**
     * 站点id
     */
    @NotBlank(message = "站点id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long stationId;

    /**
     * 运营商id
     */
    @NotBlank(message = "运营商id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long operatorId;

    /**
     * 设备号
     */
    @NotBlank(message = "设备号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long equipmentId;

    /**
     * 枪号
     */
    @NotBlank(message = "枪号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer connectorNo;

    /**
     * 枪名称
     */
    @NotBlank(message = "枪名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String connectorName;

    /**
     * 枪类型
     */
    @NotNull(message = "枪类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer connectorType;

    /**
     * 国标
     */
    @NotNull(message = "国标不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long nationalStandard;

    /**
     * 状态
     */
    private Integer status;


}
