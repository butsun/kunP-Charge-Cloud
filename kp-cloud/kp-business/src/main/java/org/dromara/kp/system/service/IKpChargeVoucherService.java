package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpChargeVoucher;
import org.dromara.kp.system.domain.vo.KpChargeVoucherVo;
import org.dromara.kp.system.domain.bo.KpChargeVoucherBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电凭证管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpChargeVoucherService {

    /**
     * 查询充电凭证管理
     *
     * @param id 主键
     * @return 充电凭证管理
     */
    KpChargeVoucherVo queryById(Long id);

    /**
     * 分页查询充电凭证管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电凭证管理分页列表
     */
    TableDataInfo<KpChargeVoucherVo> queryPageList(KpChargeVoucherBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的充电凭证管理列表
     *
     * @param bo 查询条件
     * @return 充电凭证管理列表
     */
    List<KpChargeVoucherVo> queryList(KpChargeVoucherBo bo);

    /**
     * 新增充电凭证管理
     *
     * @param bo 充电凭证管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpChargeVoucherBo bo);

    /**
     * 修改充电凭证管理
     *
     * @param bo 充电凭证管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpChargeVoucherBo bo);

    /**
     * 校验并批量删除充电凭证管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
