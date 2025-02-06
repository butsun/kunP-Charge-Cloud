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
import org.dromara.kp.system.domain.vo.KpConnectorVo;
import org.dromara.kp.system.domain.bo.KpConnectorBo;
import org.dromara.kp.system.service.IKpConnectorService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 充电枪管理
 * 前端访问路由地址为:/kpSystem/connector
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/connector")
public class KpConnectorController extends BaseController {

    private final IKpConnectorService kpConnectorService;

    /**
     * 查询充电枪管理列表
     */
    @SaCheckPermission("kpSystem:connector:list")
    @GetMapping("/list")
    public TableDataInfo<KpConnectorVo> list(KpConnectorBo bo, PageQuery pageQuery) {
        return kpConnectorService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电枪管理列表
     */
    @SaCheckPermission("kpSystem:connector:export")
    @Log(title = "充电枪管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpConnectorBo bo, HttpServletResponse response) {
        List<KpConnectorVo> list = kpConnectorService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电枪管理", KpConnectorVo.class, response);
    }

    /**
     * 获取充电枪管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:connector:query")
    @GetMapping("/{id}")
    public R<KpConnectorVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpConnectorService.queryById(id));
    }

//    /**
//     * 新增充电枪管理
//     */
//    @SaCheckPermission("kpSystem:connector:add")
//    @Log(title = "充电枪管理", businessType = BusinessType.INSERT)
//    @RepeatSubmit()
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpConnectorBo bo) {
//        return toAjax(kpConnectorService.insertByBo(bo));
//    }

//    /**
//     * 修改充电枪管理
//     */
//    @SaCheckPermission("kpSystem:connector:edit")
//    @Log(title = "充电枪管理", businessType = BusinessType.UPDATE)
//    @RepeatSubmit()
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpConnectorBo bo) {
//        return toAjax(kpConnectorService.updateByBo(bo));
//    }

    /**
     * 删除充电枪管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:connector:remove")
    @Log(title = "充电枪管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpConnectorService.deleteWithValidByIds(List.of(ids), true));
    }
}
