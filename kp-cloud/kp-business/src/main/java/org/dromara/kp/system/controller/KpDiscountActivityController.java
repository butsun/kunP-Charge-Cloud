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
import org.dromara.kp.system.domain.vo.KpDiscountActivityVo;
import org.dromara.kp.system.domain.bo.KpDiscountActivityBo;
import org.dromara.kp.system.service.IKpDiscountActivityService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 充电优惠管理
 * 前端访问路由地址为:/kpSystem/discountActivity
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/discountActivity")
public class KpDiscountActivityController extends BaseController {

    private final IKpDiscountActivityService kpDiscountActivityService;

    /**
     * 查询充电优惠管理列表
     */
    @SaCheckPermission("kpSystem:discountActivity:list")
    @GetMapping("/list")
    public TableDataInfo<KpDiscountActivityVo> list(KpDiscountActivityBo bo, PageQuery pageQuery) {
        return kpDiscountActivityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电优惠管理列表
     */
    @SaCheckPermission("kpSystem:discountActivity:export")
    @Log(title = "充电优惠管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpDiscountActivityBo bo, HttpServletResponse response) {
        List<KpDiscountActivityVo> list = kpDiscountActivityService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电优惠管理", KpDiscountActivityVo.class, response);
    }

    /**
     * 获取充电优惠管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:discountActivity:query")
    @GetMapping("/{id}")
    public R<KpDiscountActivityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpDiscountActivityService.queryById(id));
    }

    /**
     * 新增充电优惠管理
     */
    @SaCheckPermission("kpSystem:discountActivity:add")
    @Log(title = "充电优惠管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpDiscountActivityBo bo) {
        return toAjax(kpDiscountActivityService.insertByBo(bo));
    }

    /**
     * 修改充电优惠管理
     */
    @SaCheckPermission("kpSystem:discountActivity:edit")
    @Log(title = "充电优惠管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpDiscountActivityBo bo) {
        return toAjax(kpDiscountActivityService.updateByBo(bo));
    }

    /**
     * 删除充电优惠管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:discountActivity:remove")
    @Log(title = "充电优惠管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpDiscountActivityService.deleteWithValidByIds(List.of(ids), true));
    }
}
