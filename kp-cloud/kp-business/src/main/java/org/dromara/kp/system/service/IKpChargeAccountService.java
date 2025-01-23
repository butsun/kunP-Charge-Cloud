package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpChargeAccount;
import org.dromara.kp.system.domain.vo.KpChargeAccountVo;
import org.dromara.kp.system.domain.bo.KpChargeAccountBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电账户Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpChargeAccountService {

    /**
     * 查询充电账户
     *
     * @param id 主键
     * @return 充电账户
     */
    KpChargeAccountVo queryById(Long id);

    /**
     * 分页查询充电账户列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电账户分页列表
     */
    TableDataInfo<KpChargeAccountVo> queryPageList(KpChargeAccountBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的充电账户列表
     *
     * @param bo 查询条件
     * @return 充电账户列表
     */
    List<KpChargeAccountVo> queryList(KpChargeAccountBo bo);

    /**
     * 新增充电账户
     *
     * @param bo 充电账户
     * @return 是否新增成功
     */
    Boolean insertByBo(KpChargeAccountBo bo);

    /**
     * 修改充电账户
     *
     * @param bo 充电账户
     * @return 是否修改成功
     */
    Boolean updateByBo(KpChargeAccountBo bo);

    /**
     * 校验并批量删除充电账户信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
