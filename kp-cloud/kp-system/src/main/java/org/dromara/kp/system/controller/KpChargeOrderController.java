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
import org.dromara.kp.system.domain.vo.KpChargeOrderVo;
import org.dromara.kp.system.domain.bo.KpChargeOrderBo;
import org.dromara.kp.system.service.IKpChargeOrderService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 充电订单管理
 * 前端访问路由地址为:/kpSystem/chargeOrder
 *
 * @author LionLi
 * @date 2025-01-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/chargeOrder")
public class KpChargeOrderController extends BaseController {

    private final IKpChargeOrderService kpChargeOrderService;

    /**
     * 查询充电订单管理列表
     */
    @SaCheckPermission("kpSystem:chargeOrder:list")
    @GetMapping("/list")
    public TableDataInfo<KpChargeOrderVo> list(KpChargeOrderBo bo, PageQuery pageQuery) {
        return kpChargeOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电订单管理列表
     */
    @SaCheckPermission("kpSystem:chargeOrder:export")
    @Log(title = "充电订单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KpChargeOrderBo bo, HttpServletResponse response) {
        List<KpChargeOrderVo> list = kpChargeOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电订单管理", KpChargeOrderVo.class, response);
    }

    /**
     * 获取充电订单管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("kpSystem:chargeOrder:query")
    @GetMapping("/{id}")
    public R<KpChargeOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kpChargeOrderService.queryById(id));
    }

//    /**
//     * 新增充电订单管理
//     */
//    @SaCheckPermission("kpSystem:chargeOrder:add")
//    @Log(title = "充电订单管理", businessType = BusinessType.INSERT)
//    @RepeatSubmit()
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody KpChargeOrderBo bo) {
//        return toAjax(kpChargeOrderService.insertByBo(bo));
//    }

//    /**
//     * 修改充电订单管理
//     */
//    @SaCheckPermission("kpSystem:chargeOrder:edit")
//    @Log(title = "充电订单管理", businessType = BusinessType.UPDATE)
//    @RepeatSubmit()
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KpChargeOrderBo bo) {
//        return toAjax(kpChargeOrderService.updateByBo(bo));
//    }

//    /**
//     * 删除充电订单管理
//     *
//     * @param ids 主键串
//     */
//    @SaCheckPermission("kpSystem:chargeOrder:remove")
//    @Log(title = "充电订单管理", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public R<Void> remove(@NotEmpty(message = "主键不能为空")
//                          @PathVariable Long[] ids) {
//        return toAjax(kpChargeOrderService.deleteWithValidByIds(List.of(ids), true));
//    }
}
