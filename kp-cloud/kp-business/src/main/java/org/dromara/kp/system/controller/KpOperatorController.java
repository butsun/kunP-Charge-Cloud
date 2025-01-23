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
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.bo.KpOperatorBo;
import org.dromara.kp.system.service.IKpOperatorService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 运营商管理
 * 前端访问路由地址为:/kpSystem/operator
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/operator")
public class KpOperatorController extends BaseController {

    private final IKpOperatorService kpOperatorService;

    /**
     * 查询运营商管理列表
     */
    @SaCheckPermission("kpSystem:operator:list")
    @GetMapping("/list")
    public TableDataInfo<KpOperatorVo> list(KpOperatorBo bo, PageQuery pageQuery) {
        return kpOperatorService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出运营商管理列表
     */
    @SaCheckPermission("kpSystem:operator:export")
    @Log(title = "运营商管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpOperatorBo bo, HttpServletResponse response) {
        List<KpOperatorVo> list = kpOperatorService.queryList(bo);
        ExcelUtil.exportExcel(list, "运营商管理", KpOperatorVo.class, response);
    }

    /**
     * 获取运营商管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:operator:query")
    @GetMapping("/{id}")
    public R<KpOperatorVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpOperatorService.queryById(id));
    }

    /**
     * 新增运营商管理
     */
    @SaCheckPermission("kpSystem:operator:add")
    @Log(title = "运营商管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpOperatorBo bo) {
        return toAjax(kpOperatorService.insertByBo(bo));
    }

    /**
     * 修改运营商管理
     */
    @SaCheckPermission("kpSystem:operator:edit")
    @Log(title = "运营商管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpOperatorBo bo) {
        return toAjax(kpOperatorService.updateByBo(bo));
    }

    /**
     * 删除运营商管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:operator:remove")
    @Log(title = "运营商管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpOperatorService.deleteWithValidByIds(List.of(ids), true));
    }
}
