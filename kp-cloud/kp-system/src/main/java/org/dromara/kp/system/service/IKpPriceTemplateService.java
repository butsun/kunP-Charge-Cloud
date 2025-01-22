package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.kp.system.domain.request.PriceAddRequest;
import org.dromara.kp.system.domain.resposne.PriceInfoResponse;
import org.dromara.kp.system.domain.vo.KpPriceTemplateVo;
import org.dromara.kp.system.domain.bo.KpPriceTemplateBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.kp.system.domain.vo.PriceEditRequest;
import org.dromara.kp.system.domain.vo.PriceListDto;

import java.util.Collection;
import java.util.List;

/**
 * 站点价格模版Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpPriceTemplateService {


    /**
     * 查询站点价格模版
     *
     * @param bo priceCode
     * @return 价格模版列表
     */
    TableDataInfo<PriceListDto> getPricePage(KpPriceTemplateBo bo, PageQuery pageQuery);


    /**
     * 根据priceCOde获取指定价格模版list
     * @param priceCode
     * @return
     */
    PriceInfoResponse getPriceInfo(Long priceCode);


    /**
     * 根据站点id获取指定价格模版list
     * @param stationId
     * @return
     */
    PriceInfoResponse getStationPriceInfo(Long stationId);


    /**
     * 删除指定priceCode的价格模版
     * @param priceCode priceCode
     */
    void removePrice(Long priceCode);


    /**
     * 校验并批量删除站点价格模版信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 添加价格模版
     * @param priceAddRequest priceAddRequest
     * @return  Long PriceCode
     */
    Long addPrice(PriceAddRequest priceAddRequest);

    /**
     * 获取最大的priceCode
     * @return Long
     */
    Long getMaxPriceCode();


    void editPrice(PriceEditRequest priceEditRequest);
}
