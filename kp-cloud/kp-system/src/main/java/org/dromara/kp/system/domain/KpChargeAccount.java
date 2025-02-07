package org.dromara.kp.system.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 充电账户对象 kp_charge_account
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kp_charge_account")
public class KpChargeAccount extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 账户类型
     */
    private Integer accoutType;

    /**
     * 禁用
     */
    private Integer disableFlag;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 最近一次访问时间
     */
    private Date lastVisitTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据状态:0、正常;1、删除
     */
    @TableLogic
    private Long delFlag;


}
