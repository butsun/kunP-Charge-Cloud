package org.dromara.kp.system.domain.bo;

import org.dromara.kp.system.domain.KpEquipment;
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
 * 充电设备管理业务对象 kp_equipment
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KpEquipment.class, reverseConvertGenerate = false)
public class KpEquipmentBo extends BaseEntity {

    private Long id;

    /**
     * 站点id
     */
    @NotNull(message = "站点id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long stationId;

    /**
     * 运营商id
     */
    @NotNull(message = "运营商id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long operatorId;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String equipmentNo;

    /**
     * 设备生产商名称
     */
    private String manufacturerName;

    /**
     * 设备型号
     */
    private String equipmentModel;

    /**
     * 额定电压上限(单位:V)
     */
    private Integer voltageUpperLimits;

    /**
     * 额定电压下限(单位:V)
     */
    private Integer voltageLowerLimits;

    /**
     * 额定电流
     */
    @NotNull(message = "额定电流不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer currentValue;

    /**
     * 额定功率
     */
    @NotNull(message = "额定功率不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer power;

    /**
     * 设备类型
     */
    @NotNull(message = "设备类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer equipmentType;

    @NotNull(message = "枪数不能为空", groups = {AddGroup.class})
    private Integer gunSum;

    /**
     * 车位号
     */
    private String parkNo;

    /**
     * 工作状态
     */
    private Integer isWorking;


}
