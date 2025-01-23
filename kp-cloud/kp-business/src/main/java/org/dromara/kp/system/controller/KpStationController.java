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
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.domain.bo.KpStationBo;
import org.dromara.kp.system.service.IKpStationService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 站点管理
 * 前端访问路由地址为:/kpSystem/station
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/station")
public class KpStationController extends BaseController {

    private final IKpStationService kpStationService;

    /**
     * 查询站点管理列表
     */
    @SaCheckPermission("kpSystem:station:list")
    @GetMapping("/list")
    public TableDataInfo<KpStationVo> list(KpStationBo bo, PageQuery pageQuery) {
        return kpStationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出站点管理列表
     */
    @SaCheckPermission("kpSystem:station:export")
    @Log(title = "站点管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpStationBo bo, HttpServletResponse response) {
        List<KpStationVo> list = kpStationService.queryList(bo);
        ExcelUtil.exportExcel(list, "站点管理", KpStationVo.class, response);
    }

    /**
     * 获取站点管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:station:query")
    @GetMapping("/{id}")
    public R<KpStationVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpStationService.queryById(id));
    }

    /**
     * 新增站点管理
     */
    @SaCheckPermission("kpSystem:station:add")
    @Log(title = "站点管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpStationBo bo) {
        return toAjax(kpStationService.insertByBo(bo));
    }

    /**
     * 修改站点管理
     */
    @SaCheckPermission("kpSystem:station:edit")
    @Log(title = "站点管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpStationBo bo) {
        return toAjax(kpStationService.updateByBo(bo));
    }

    /**
     * 删除站点管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:station:remove")
    @Log(title = "站点管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpStationService.deleteWithValidByIds(List.of(ids), true));
    }
}
