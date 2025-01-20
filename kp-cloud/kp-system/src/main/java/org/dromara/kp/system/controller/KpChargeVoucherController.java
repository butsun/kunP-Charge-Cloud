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
import org.dromara.kp.system.domain.vo.KpChargeVoucherVo;
import org.dromara.kp.system.domain.bo.KpChargeVoucherBo;
import org.dromara.kp.system.service.IKpChargeVoucherService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 充电凭证管理
 * 前端访问路由地址为:/kpSystem/chargeVoucher
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/chargeVoucher")
public class KpChargeVoucherController extends BaseController {

    private final IKpChargeVoucherService kpChargeVoucherService;

    /**
     * 查询充电凭证管理列表
     */
    @SaCheckPermission("kpSystem:chargeVoucher:list")
    @GetMapping("/list")
    public TableDataInfo<KpChargeVoucherVo> list(KpChargeVoucherBo bo, PageQuery pageQuery) {
        return kpChargeVoucherService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电凭证管理列表
     */
    @SaCheckPermission("kpSystem:chargeVoucher:export")
    @Log(title = "充电凭证管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpChargeVoucherBo bo, HttpServletResponse response) {
        List<KpChargeVoucherVo> list = kpChargeVoucherService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电凭证管理", KpChargeVoucherVo.class, response);
    }

    /**
     * 获取充电凭证管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:chargeVoucher:query")
    @GetMapping("/{id}")
    public R<KpChargeVoucherVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpChargeVoucherService.queryById(id));
    }

    /**
     * 新增充电凭证管理
     */
    @SaCheckPermission("kpSystem:chargeVoucher:add")
    @Log(title = "充电凭证管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpChargeVoucherBo bo) {
        return toAjax(kpChargeVoucherService.insertByBo(bo));
    }

    /**
     * 修改充电凭证管理
     */
    @SaCheckPermission("kpSystem:chargeVoucher:edit")
    @Log(title = "充电凭证管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpChargeVoucherBo bo) {
        return toAjax(kpChargeVoucherService.updateByBo(bo));
    }

    /**
     * 删除充电凭证管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:chargeVoucher:remove")
    @Log(title = "充电凭证管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpChargeVoucherService.deleteWithValidByIds(List.of(ids), true));
    }
}
