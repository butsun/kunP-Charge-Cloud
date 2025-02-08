package org.dromara.kp.system.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.dromara.common.core.exception.base.BaseException;
import org.dromara.kp.system.domain.bo.KpOperatorBo;
import org.dromara.kp.system.domain.request.PriceAddRequest;
import org.dromara.kp.system.domain.vo.*;
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
@Slf4j
public class KpPriceTemplateController extends BaseController {

    private final IKpPriceTemplateService kpPriceTemplateService;


    /**
     * 获取充电价格模版列表，可以根据备注信息搜索
     *
     * @param bo        bo
     * @param pageQuery pageQuery
     * @return R
     */
    @SaCheckPermission("kpSystem:priceTemplate:list")
    @GetMapping(value = "/list")
    @Operation(summary = "获取充电价格模版列表，可以根据备注信息搜索")
    public TableDataInfo<KpPriceTemplateVo> getPriceList(KpPriceTemplateBo bo, PageQuery pageQuery) {
        return kpPriceTemplateService.queryPageList(bo, pageQuery);
    }



    /**
     * 模糊查询价格模版管理列表
     */
    @SaCheckPermission("kpSystem:priceTemplate:list")
    @GetMapping("/like")
    public TableDataInfo<KpPriceTemplateVo> likeList(KpPriceTemplateBo bo) {
        return TableDataInfo.build(kpPriceTemplateService.queryList(bo));
    }


    /**
     * 导出价格模版管理列表
     */
    @SaCheckPermission("kpSystem:priceTemplate:export")
    @Log(title = "价格模版管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpPriceTemplateBo bo, HttpServletResponse response) {
        List<KpPriceTemplateVo> list = kpPriceTemplateService.queryList(bo);
        ExcelUtil.exportExcel(list, "价格模版管理", KpPriceTemplateVo.class, response);
    }


    /**
     * 获取价格模版管理详细信息
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
     * 新增价格模版管理
     */
    @SaCheckPermission("kpSystem:priceTemplate:add")
    @Log(title = "价格模版管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpPriceTemplateBo bo) {
        return toAjax(kpPriceTemplateService.insertByBo(bo));
    }


    /**
     * 修改价格模版管理
     */
    @SaCheckPermission("kpSystem:priceTemplate:edit")
    @Log(title = "价格模版管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpPriceTemplateBo bo) {
        return toAjax(kpPriceTemplateService.updateByBo(bo));
    }

    /**
     * 删除价格模版管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("kpSystem:priceTemplate:remove")
    @Log(title = "价格模版管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kpPriceTemplateService.deleteWithValidByIds(List.of(ids), true));
    }


    /**
     * 根据站点id获取价格模版
     *
     * @param stationId stationId
     * @return R
     */
    @GetMapping(value = "/getStationPrice/{stationId}")
    @Operation(summary = "根据充电站，获取价格")
    public R getStationPrice(@PathVariable Long stationId) {
        try {
            return R.ok(kpPriceTemplateService.getStationPriceInfo(stationId));
        } catch (BaseException ex) {
            log.error(ex.getMessage(), ex);
            return R.fail("内部服务错误");
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return R.fail("内部服务错误");
        }
    }
}
