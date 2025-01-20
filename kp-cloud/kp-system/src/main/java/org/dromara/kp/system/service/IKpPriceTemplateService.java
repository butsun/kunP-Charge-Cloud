package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.kp.system.domain.vo.KpPriceTemplateVo;
import org.dromara.kp.system.domain.bo.KpPriceTemplateBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

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
     * @param id 主键
     * @return 站点价格模版
     */
    KpPriceTemplateVo queryById(Long id);

    /**
     * 分页查询站点价格模版列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 站点价格模版分页列表
     */
    TableDataInfo<KpPriceTemplateVo> queryPageList(KpPriceTemplateBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的站点价格模版列表
     *
     * @param bo 查询条件
     * @return 站点价格模版列表
     */
    List<KpPriceTemplateVo> queryList(KpPriceTemplateBo bo);

    /**
     * 新增站点价格模版
     *
     * @param bo 站点价格模版
     * @return 是否新增成功
     */
    Boolean insertByBo(KpPriceTemplateBo bo);

    /**
     * 修改站点价格模版
     *
     * @param bo 站点价格模版
     * @return 是否修改成功
     */
    Boolean updateByBo(KpPriceTemplateBo bo);

    /**
     * 校验并批量删除站点价格模版信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
