package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.bo.KpEquipmentBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电设备管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpEquipmentService {

    /**
     * 查询充电设备管理
     *
     * @param id 主键
     * @return 充电设备管理
     */
    KpEquipmentVo queryById(Long id);

    /**
     * 分页查询充电设备管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电设备管理分页列表
     */
    TableDataInfo<KpEquipmentVo> queryPageList(KpEquipmentBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的充电设备管理列表
     *
     * @param bo 查询条件
     * @return 充电设备管理列表
     */
    List<KpEquipmentVo> queryList(KpEquipmentBo bo);

    /**
     * 新增充电设备管理
     *
     * @param bo 充电设备管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpEquipmentBo bo);

    /**
     * 修改充电设备管理
     *
     * @param bo 充电设备管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpEquipmentBo bo);

    /**
     * 校验并批量删除充电设备管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据设备编号精确查找
     * @param pileCode 设备编号
     * @return KpEquipmentVo
     */
    KpEquipment queryByEquipmentNo(String pileCode);

    void update(KpEquipment equipment );

    void pileLost(String pileCode);
}
