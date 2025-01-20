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
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.bo.KpEquipmentBo;
import org.dromara.kp.system.service.IKpEquipmentService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 充电设备管理
 * 前端访问路由地址为:/kpSystem/equipment
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/equipment")
public class KpEquipmentController extends BaseController {

    private final IKpEquipmentService kpEquipmentService;

    /**
     * 查询充电设备管理列表
     */
    @SaCheckPermission("kpSystem:equipment:list")
    @GetMapping("/list")
    public TableDataInfo<KpEquipmentVo> list(KpEquipmentBo bo, PageQuery pageQuery) {
        return kpEquipmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电设备管理列表
     */
    @SaCheckPermission("kpSystem:equipment:export")
    @Log(title = "充电设备管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpEquipmentBo bo, HttpServletResponse response) {
        List<KpEquipmentVo> list = kpEquipmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电设备管理", KpEquipmentVo.class, response);
    }

    /**
     * 获取充电设备管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:equipment:query")
    @GetMapping("/{id}")
    public R<KpEquipmentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpEquipmentService.queryById(id));
    }

    /**
     * 新增充电设备管理
     */
    @SaCheckPermission("kpSystem:equipment:add")
    @Log(title = "充电设备管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpEquipmentBo bo) {
        return toAjax(kpEquipmentService.insertByBo(bo));
    }

    /**
     * 修改充电设备管理
     */
    @SaCheckPermission("kpSystem:equipment:edit")
    @Log(title = "充电设备管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpEquipmentBo bo) {
        return toAjax(kpEquipmentService.updateByBo(bo));
    }

    /**
     * 删除充电设备管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:equipment:remove")
    @Log(title = "充电设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpEquipmentService.deleteWithValidByIds(List.of(ids), true));
    }
}
