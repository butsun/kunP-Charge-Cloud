package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpOperator;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.bo.KpOperatorBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 运营商管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpOperatorService {

    /**
     * 查询运营商管理
     *
     * @param id 主键
     * @return 运营商管理
     */
    KpOperatorVo queryById(Long id);

    /**
     * 分页查询运营商管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 运营商管理分页列表
     */
    TableDataInfo<KpOperatorVo> queryPageList(KpOperatorBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的运营商管理列表
     *
     * @param bo 查询条件
     * @return 运营商管理列表
     */
    List<KpOperatorVo> queryList(KpOperatorBo bo);

    /**
     * 新增运营商管理
     *
     * @param bo 运营商管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpOperatorBo bo);

    /**
     * 修改运营商管理
     *
     * @param bo 运营商管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpOperatorBo bo);

    /**
     * 校验并批量删除运营商管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
