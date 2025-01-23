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
import org.dromara.kp.system.domain.request.PriceAddRequest;
import org.dromara.kp.system.domain.vo.PriceEditRequest;
import org.dromara.kp.system.domain.vo.PriceInfoData;
import org.dromara.kp.system.domain.vo.PriceListDto;
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
    public TableDataInfo<PriceListDto> getPriceList(KpPriceTemplateBo bo, PageQuery pageQuery) {
        return kpPriceTemplateService.getPricePage(bo, pageQuery);
    }


    /**
     * 根据价格Code，获取价格
     *
     * @param priceCode priceCode
     * @return R
     */
    @GetMapping(value = "/{priceCode}")
    @Operation(summary = "根据价格Code，获取价格")
    public R getPrice(@PathVariable Long priceCode) {
        try {
            if (priceCode == null || priceCode < 0) {
                throw new BaseException("无效的价格编码");
            }
            return R.ok(kpPriceTemplateService.getPriceInfo(priceCode));
        } catch (BaseException ex) {
            log.error(ex.getMessage(), ex);
            return R.fail("内部服务错误");
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return R.fail("内部服务错误");
        }
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


    /**
     * 新增站点价格模版
     */
    @SaCheckPermission("kpSystem:priceTemplate:add")
    @Log(title = "站点价格模版", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated @RequestBody PriceAddRequest priceAddRequest) {
        kpPriceTemplateService.addPrice(priceAddRequest);
        return R.ok("ok");
    }

    /**
     * 修改站点价格模版
     */
    @SaCheckPermission("kpSystem:priceTemplate:edit")
    @Log(title = "站点价格模版", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@RequestBody PriceEditRequest priceEditRequest) {
        kpPriceTemplateService.editPrice(priceEditRequest);
        return R.ok("ok");
    }


    /**
     * 删除价格code
     *
     * @param priceCode priceCode
     * @return R
     */
    @DeleteMapping(value = "/{priceCode}")
    @Operation(summary = "删除价格Code")
    public R del(@PathVariable Long priceCode) {
        try {
            if (priceCode == null || priceCode < 0) {
                throw new BaseException("无效的价格编码");
            } else if (priceCode == 0) {
                throw new BaseException("默认价格模版不可删除");
            }
            kpPriceTemplateService.removePrice(priceCode);
            return R.ok("已删除");
        } catch (BaseException ex) {
            log.error(ex.getMessage(), ex);
            return R.fail("内部服务错误");
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return R.fail("内部服务错误");
        }
    }
}
