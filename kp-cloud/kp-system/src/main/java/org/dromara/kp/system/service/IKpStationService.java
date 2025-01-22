package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpStation;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.domain.bo.KpStationBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 站点管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpStationService {

    /**
     * 查询站点管理
     *
     * @param id 主键
     * @return 站点管理
     */
    KpStationVo queryById(Long id);

    /**
     * 分页查询站点管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 站点管理分页列表
     */
    TableDataInfo<KpStationVo> queryPageList(KpStationBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的站点管理列表
     *
     * @param bo 查询条件
     * @return 站点管理列表
     */
    List<KpStationVo> queryList(KpStationBo bo);

    /**
     * 新增站点管理
     *
     * @param bo 站点管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpStationBo bo);

    /**
     * 修改站点管理
     *
     * @param bo 站点管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpStationBo bo);

    /**
     * 校验并批量删除站点管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查找是否有站点关联价格模版
     * @param priceCode priceCode
     * @return List
     */
    List<Long> getLinkStations(Long priceCode);
}
