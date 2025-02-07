package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpConnector;
import org.dromara.kp.system.domain.vo.KpConnectorVo;
import org.dromara.kp.system.domain.bo.KpConnectorBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电枪管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpConnectorService {

    /**
     * 查询充电枪管理
     *
     * @param id 主键
     * @return 充电枪管理
     */
    KpConnectorVo queryById(Long id);

    /**
     * 分页查询充电枪管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电枪管理分页列表
     */
    TableDataInfo<KpConnectorVo> queryPageList(KpConnectorBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的充电枪管理列表
     *
     * @param bo 查询条件
     * @return 充电枪管理列表
     */
    List<KpConnectorVo> queryList(KpConnectorBo bo);

    /**
     * 新增充电枪管理
     *
     * @param bo 充电枪管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpConnectorBo bo);

    /**
     * 修改充电枪管理
     *
     * @param bo 充电枪管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpConnectorBo bo);

    /**
     * 校验并批量删除充电枪管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据设备修改信息修改枪信息
     * @param bo  充电枪管理
     * @return 是否删除成功
     */
    Boolean updateByEquipmentId(KpConnectorBo bo);
}
