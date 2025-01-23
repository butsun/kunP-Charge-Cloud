package org.dromara.kp.system.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.kp.system.domain.vo.KpChargeAccountVo;
import org.dromara.kp.system.domain.bo.KpChargeAccountBo;
import org.dromara.kp.system.service.IKpChargeAccountService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 充电账户
 * 前端访问路由地址为:/kpSystem/chargeAccount
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/chargeAccount")
public class KpChargeAccountController extends BaseController {

    private final IKpChargeAccountService kpChargeAccountService;

    /**
     * 查询充电账户列表
     */
    @SaCheckPermission("kpSystem:chargeAccount:list")
    @GetMapping("/list")
    public TableDataInfo<KpChargeAccountVo> list(KpChargeAccountBo bo, PageQuery pageQuery) {
        return kpChargeAccountService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电账户列表
     */
    @SaCheckPermission("kpSystem:chargeAccount:export")
    @Log(title = "充电账户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpChargeAccountBo bo, HttpServletResponse response) {
        List<KpChargeAccountVo> list = kpChargeAccountService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电账户", KpChargeAccountVo.class, response);
    }

    /**
     * 获取充电账户详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:chargeAccount:query")
    @GetMapping("/{id}")
    public R<KpChargeAccountVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpChargeAccountService.queryById(id));
    }

    /**
     * 新增充电账户
     */
    @SaCheckPermission("kpSystem:chargeAccount:add")
    @Log(title = "充电账户", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpChargeAccountBo bo) {
        return toAjax(kpChargeAccountService.insertByBo(bo));
    }

    /**
     * 修改充电账户
     */
    @SaCheckPermission("kpSystem:chargeAccount:edit")
    @Log(title = "充电账户", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpChargeAccountBo bo) {
        return toAjax(kpChargeAccountService.updateByBo(bo));
    }

    /**
     * 删除充电账户
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:chargeAccount:remove")
    @Log(title = "充电账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpChargeAccountService.deleteWithValidByIds(List.of(ids), true));
    }
}
