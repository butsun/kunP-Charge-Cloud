package org.dromara.kp.system.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpEquipmentBo;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.mapper.KpEquipmentMapper;
import org.dromara.kp.system.service.IKpEquipmentService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;

/**
 * 充电设备管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpEquipmentServiceImpl implements IKpEquipmentService {

    private final KpEquipmentMapper baseMapper;

    /**
     * 查询充电设备管理
     *
     * @param id 主键
     * @return 充电设备管理
     */
    @Override
    public KpEquipmentVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询充电设备管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电设备管理分页列表
     */
    @Override
    public TableDataInfo<KpEquipmentVo> queryPageList(KpEquipmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpEquipment> lqw = buildQueryWrapper(bo);
        Page<KpEquipmentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的充电设备管理列表
     *
     * @param bo 查询条件
     * @return 充电设备管理列表
     */
    @Override
    public List<KpEquipmentVo> queryList(KpEquipmentBo bo) {
        LambdaQueryWrapper<KpEquipment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KpEquipment> buildQueryWrapper(KpEquipmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpEquipment> lqw = Wrappers.lambdaQuery();
        lqw.eq(Objects.nonNull(bo.getStationId()), KpEquipment::getStationId, bo.getStationId());
        lqw.like(StringUtils.isNotBlank(bo.getManufacturerName()), KpEquipment::getManufacturerName, bo.getManufacturerName());
        lqw.eq(StringUtils.isNotBlank(bo.getEquipmentModel()), KpEquipment::getEquipmentModel, bo.getEquipmentModel());
        lqw.like(StringUtils.isNotBlank(bo.getEquipmentName()), KpEquipment::getEquipmentName, bo.getEquipmentName());
        return lqw;
    }

    /**
     * 新增充电设备管理
     *
     * @param bo 充电设备管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpEquipmentBo bo) {
        KpEquipment add = MapstructUtils.convert(bo, KpEquipment.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电设备管理
     *
     * @param bo 充电设备管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpEquipmentBo bo) {
        KpEquipment update = MapstructUtils.convert(bo, KpEquipment.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpEquipment entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除充电设备管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
