package org.dromara.kp.system.service.impl;

import cn.hutool.core.date.DateUtil;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.mapper.KpEquipmentMapper;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.dromara.kp.system.mapper.KpStationMapper;
import org.dromara.kp.system.service.IKpEquipmentService;
import org.dromara.kp.system.service.IKpOperatorService;
import org.dromara.kp.system.service.IKpStationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpConnectorBo;
import org.dromara.kp.system.domain.vo.KpConnectorVo;
import org.dromara.kp.system.domain.KpConnector;
import org.dromara.kp.system.mapper.KpConnectorMapper;
import org.dromara.kp.system.service.IKpConnectorService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;

/**
 * 充电枪管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpConnectorServiceImpl implements IKpConnectorService {

    private final KpConnectorMapper baseMapper;

    private final KpEquipmentMapper equipmentMapper;

    private final KpOperatorMapper operatorMapper;

    private final KpStationMapper stationMapper;
    /**
     * 查询充电枪管理
     *
     * @param id 主键
     * @return 充电枪管理
     */
    @Override
    public KpConnectorVo queryById(Long id) {
        KpConnectorVo vo = baseMapper.selectVoById(id);
        return getKpConnectorVo(vo);
    }

    @NotNull
    private KpConnectorVo getKpConnectorVo(KpConnectorVo vo) {
        KpEquipmentVo kpEquipmentVo = equipmentMapper.selectVoById(vo.getEquipmentId());
        KpOperatorVo kpOperatorVo = operatorMapper.selectVoById(vo.getOperatorId());
        KpStationVo kpStationVo = stationMapper.selectVoById(vo.getStationId());
        vo.setOperatorName(kpOperatorVo.getOperatorName());
        vo.setStationName(kpStationVo.getStationName());
        vo.setEquipmentNo(kpEquipmentVo.getEquipmentNo());
        vo.setEquipmentType(kpEquipmentVo.getEquipmentType());
        vo.setCurrentValue(kpEquipmentVo.getCurrentValue());
        vo.setNetType(kpEquipmentVo.getNetType());
        vo.setPower(kpEquipmentVo.getPower());
        return vo;
    }

    /**
     * 分页查询充电枪管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电枪管理分页列表
     */
    @Override
    public TableDataInfo<KpConnectorVo> queryPageList(KpConnectorBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpConnector> lqw = buildQueryWrapper(bo);
        Page<KpConnectorVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::getKpConnectorVo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的充电枪管理列表
     *
     * @param bo 查询条件
     * @return 充电枪管理列表
     */
    @Override
    public List<KpConnectorVo> queryList(KpConnectorBo bo) {
        LambdaQueryWrapper<KpConnector> lqw = buildQueryWrapper(bo);
        List<KpConnectorVo> vos = baseMapper.selectVoList(lqw);
        vos.forEach(this::getKpConnectorVo);
        return vos;
    }

    private LambdaQueryWrapper<KpConnector> buildQueryWrapper(KpConnectorBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpConnector> lqw = Wrappers.lambdaQuery();
        lqw.eq(Objects.nonNull(bo.getStationId()), KpConnector::getStationId, bo.getStationId());
        lqw.eq(Objects.nonNull(bo.getOperatorId()), KpConnector::getOperatorId, bo.getOperatorId());
        lqw.eq(Objects.nonNull(bo.getEquipmentId()), KpConnector::getEquipmentId, bo.getEquipmentId());
        lqw.eq(Objects.nonNull(bo.getConnectorNo()), KpConnector::getConnectorNo, bo.getConnectorNo());
        return lqw;
    }

    /**
     * 新增充电枪管理
     *
     * @param bo 充电枪管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpConnectorBo bo) {
        KpConnector add = MapstructUtils.convert(bo, KpConnector.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电枪管理
     *
     * @param bo 充电枪管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpConnectorBo bo) {
        KpConnector update = MapstructUtils.convert(bo, KpConnector.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpConnector entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除充电枪管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public Boolean updateByEquipmentId(KpConnectorBo bo) {
        return baseMapper.update(Wrappers.<KpConnector>lambdaUpdate()
            .eq(KpConnector::getEquipmentId, bo.getEquipmentId())
            .set(Objects.nonNull(bo.getStationId()), KpConnector::getStationId, bo.getStationId())
            .set(Objects.nonNull(bo.getOperatorId()), KpConnector::getOperatorId, bo.getOperatorId())
            .set(BaseEntity::getUpdateTime, DateUtil.date())) > 0;
    }

    @Override
    public KpConnector queryByNo(String pileCode, Integer gunNo) {
        return baseMapper.selectOne(Wrappers.<KpConnector>lambdaUpdate()
            .eq(KpConnector::getEquipmentNo, pileCode)
            .eq(KpConnector::getConnectorNo, gunNo));
    }

    @Override
    public void updateStatus(String pileCode, int gunNo, Integer gunState) {
        baseMapper.update(Wrappers.<KpConnector>lambdaUpdate()
            .eq(KpConnector::getEquipmentNo, pileCode)
            .eq(KpConnector::getConnectorNo, gunNo)
            .set(KpConnector::getStatus, gunState)
            .set(BaseEntity::getUpdateTime, DateUtil.date()));
    }
}
