package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpChargeOrder;
import org.dromara.kp.system.domain.vo.KpChargeOrderVo;
import org.dromara.kp.system.domain.bo.KpChargeOrderBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电订单管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpChargeOrderService {

    /**
     * 查询充电订单管理
     *
     * @param id 主键
     * @return 充电订单管理
     */
    KpChargeOrderVo queryById(Long id);

    /**
     * 分页查询充电订单管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电订单管理分页列表
     */
    TableDataInfo<KpChargeOrderVo> queryPageList(KpChargeOrderBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的充电订单管理列表
     *
     * @param bo 查询条件
     * @return 充电订单管理列表
     */
    List<KpChargeOrderVo> queryList(KpChargeOrderBo bo);

    /**
     * 新增充电订单管理
     *
     * @param bo 充电订单管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpChargeOrderBo bo);

    /**
     * 修改充电订单管理
     *
     * @param bo 充电订单管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpChargeOrderBo bo);

    /**
     * 校验并批量删除充电订单管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     *
     * @param tradeNo 流水号
     * @param pileCode 设备号
     * @param gunCode 枪号
     * @return KpChargeOrder
     */
    KpChargeOrder queryByTradeNo(String tradeNo, String pileCode, int gunCode);

    boolean insertOrder(KpChargeOrder kpChargeOrder);

    boolean refreshOrder(KpChargeOrder chargeOrder);
}
