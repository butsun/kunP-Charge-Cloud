package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpDiscountActivity;
import org.dromara.kp.system.domain.vo.KpDiscountActivityVo;
import org.dromara.kp.system.domain.bo.KpDiscountActivityBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电优惠管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpDiscountActivityService {

    /**
     * 查询充电优惠管理
     *
     * @param id 主键
     * @return 充电优惠管理
     */
    KpDiscountActivityVo queryById(Long id);

    /**
     * 分页查询充电优惠管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电优惠管理分页列表
     */
    TableDataInfo<KpDiscountActivityVo> queryPageList(KpDiscountActivityBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的充电优惠管理列表
     *
     * @param bo 查询条件
     * @return 充电优惠管理列表
     */
    List<KpDiscountActivityVo> queryList(KpDiscountActivityBo bo);

    /**
     * 新增充电优惠管理
     *
     * @param bo 充电优惠管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpDiscountActivityBo bo);

    /**
     * 修改充电优惠管理
     *
     * @param bo 充电优惠管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpDiscountActivityBo bo);

    /**
     * 校验并批量删除充电优惠管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    KpDiscountActivity queryByOperatorId(Long operatorId,Integer accountType);
}
