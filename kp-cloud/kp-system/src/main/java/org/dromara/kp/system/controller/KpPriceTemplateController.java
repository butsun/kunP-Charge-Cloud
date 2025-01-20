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
import org.dromara.kp.system.domain.vo.KpPriceTemplateVo;
import org.dromara.kp.system.domain.bo.KpPriceTemplateBo;
import org.dromara.kp.system.service.IKpPriceTemplateService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 站点价格模版
 * 前端访问路由地址为:/kpSystem/priceTemplate
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/priceTemplate")
public class KpPriceTemplateController extends BaseController {

    private final IKpPriceTemplateService kpPriceTemplateService;

    /**
     * 查询站点价格模版列表
     */
    @SaCheckPermission("kpSystem:priceTemplate:list")
    @GetMapping("/list")
    public TableDataInfo<KpPriceTemplateVo> list(KpPriceTemplateBo bo, PageQuery pageQuery) {
        return kpPriceTemplateService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出站点价格模版列表
     */
    @SaCheckPermission("kpSystem:priceTemplate:export")
    @Log(title = "站点价格模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpPriceTemplateBo bo, HttpServletResponse response) {
        List<KpPriceTemplateVo> list = kpPriceTemplateService.queryList(bo);
        ExcelUtil.exportExcel(list, "站点价格模版", KpPriceTemplateVo.class, response);
    }

    /**
     * 获取站点价格模版详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:priceTemplate:query")
    @GetMapping("/{id}")
    public R<KpPriceTemplateVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpPriceTemplateService.queryById(id));
    }

    /**
     * 新增站点价格模版
     */
    @SaCheckPermission("kpSystem:priceTemplate:add")
    @Log(title = "站点价格模版", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpPriceTemplateBo bo) {
        return toAjax(kpPriceTemplateService.insertByBo(bo));
    }

    /**
     * 修改站点价格模版
     */
    @SaCheckPermission("kpSystem:priceTemplate:edit")
    @Log(title = "站点价格模版", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpPriceTemplateBo bo) {
        return toAjax(kpPriceTemplateService.updateByBo(bo));
    }

    /**
     * 删除站点价格模版
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:priceTemplate:remove")
    @Log(title = "站点价格模版", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpPriceTemplateService.deleteWithValidByIds(List.of(ids), true));
    }
}
