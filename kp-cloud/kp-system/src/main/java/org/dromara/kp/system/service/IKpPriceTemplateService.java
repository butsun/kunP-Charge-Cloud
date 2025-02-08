package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.kp.system.domain.bo.KpPriceTemplateBo;
import org.dromara.kp.system.domain.request.PriceAddRequest;
import org.dromara.kp.system.domain.resposne.PriceInfoResponse;
import org.dromara.kp.system.domain.vo.KpPriceTemplateVo;
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
     * 查询价格模版管理
     *
     * @param id 主键
     * @return 价格模版管理
     */
    KpPriceTemplateVo queryById(Long id);

    /**
     * 分页查询价格模版管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 价格模版管理分页列表
     */
    TableDataInfo<KpPriceTemplateVo> queryPageList(KpPriceTemplateBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的价格模版管理列表
     *
     * @param bo 查询条件
     * @return 价格模版管理列表
     */
    List<KpPriceTemplateVo> queryList(KpPriceTemplateBo bo);

    /**
     * 新增价格模版管理
     *
     * @param bo 价格模版管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpPriceTemplateBo bo);

    /**
     * 修改价格模版管理
     *
     * @param bo 价格模版管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpPriceTemplateBo bo);

    /**
     * 根据站点id获取指定价格模版list
     * @param stationId
     * @return
     */
    PriceInfoResponse getStationPriceInfo(Long stationId);
    /**
     * 校验并批量删除价格模版管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    KpPriceTemplate queryBYStationId(Long stationId);
}
