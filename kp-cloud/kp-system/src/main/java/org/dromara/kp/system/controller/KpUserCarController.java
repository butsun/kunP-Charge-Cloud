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
import org.dromara.kp.system.domain.vo.KpUserCarVo;
import org.dromara.kp.system.domain.bo.KpUserCarBo;
import org.dromara.kp.system.service.IKpUserCarService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 车辆管理
 * 前端访问路由地址为:/kpSystem/userCar
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/userCar")
public class KpUserCarController extends BaseController {

    private final IKpUserCarService kpUserCarService;

    /**
     * 查询车辆管理列表
     */
    @SaCheckPermission("kpSystem:userCar:list")
    @GetMapping("/list")
    public TableDataInfo<KpUserCarVo> list(KpUserCarBo bo, PageQuery pageQuery) {
        return kpUserCarService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出车辆管理列表
     */
    @SaCheckPermission("kpSystem:userCar:export")
    @Log(title = "车辆管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpUserCarBo bo, HttpServletResponse response) {
        List<KpUserCarVo> list = kpUserCarService.queryList(bo);
        ExcelUtil.exportExcel(list, "车辆管理", KpUserCarVo.class, response);
    }

    /**
     * 获取车辆管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:userCar:query")
    @GetMapping("/{id}")
    public R<KpUserCarVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpUserCarService.queryById(id));
    }

    /**
     * 新增车辆管理
     */
    @SaCheckPermission("kpSystem:userCar:add")
    @Log(title = "车辆管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpUserCarBo bo) {
        return toAjax(kpUserCarService.insertByBo(bo));
    }

    /**
     * 修改车辆管理
     */
    @SaCheckPermission("kpSystem:userCar:edit")
    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpUserCarBo bo) {
        return toAjax(kpUserCarService.updateByBo(bo));
    }

    /**
     * 删除车辆管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:userCar:remove")
    @Log(title = "车辆管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpUserCarService.deleteWithValidByIds(List.of(ids), true));
    }
}
