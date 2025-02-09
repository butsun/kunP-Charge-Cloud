package org.dromara.kp.system.service;

import org.dromara.kp.system.domain.KpUserCar;
import org.dromara.kp.system.domain.vo.KpUserCarVo;
import org.dromara.kp.system.domain.bo.KpUserCarBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 车辆管理Service接口
 *
 * @author LionLi
 * @date 2025-01-20
 */
public interface IKpUserCarService {

    /**
     * 查询车辆管理
     *
     * @param id 主键
     * @return 车辆管理
     */
    KpUserCarVo queryById(Long id);

    /**
     * 分页查询车辆管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 车辆管理分页列表
     */
    TableDataInfo<KpUserCarVo> queryPageList(KpUserCarBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的车辆管理列表
     *
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    List<KpUserCarVo> queryList(KpUserCarBo bo);

    /**
     * 新增车辆管理
     *
     * @param bo 车辆管理
     * @return 是否新增成功
     */
    Boolean insertByBo(KpUserCarBo bo);

    /**
     * 修改车辆管理
     *
     * @param bo 车辆管理
     * @return 是否修改成功
     */
    Boolean updateByBo(KpUserCarBo bo);

    /**
     * 校验并批量删除车辆管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    KpUserCar queryByVin(String vin);
}
