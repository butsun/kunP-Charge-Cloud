package org.dromara.kp.system.service.impl;

import cn.hutool.core.date.DateUtil;
import org.dromara.common.core.exception.base.BaseException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.kp.system.domain.KpConnector;
import org.dromara.kp.system.domain.bo.KpConnectorBo;
import org.dromara.kp.system.domain.vo.KpConnectorVo;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.mapper.KpConnectorMapper;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.dromara.kp.system.mapper.KpStationMapper;
import org.dromara.kp.system.service.IKpConnectorService;
import org.dromara.kp.system.service.IKpOperatorService;
import org.dromara.kp.system.service.IKpStationService;
import org.jetbrains.annotations.NotNull;
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

    private final KpConnectorMapper connectorMapper;

    private final KpOperatorMapper operatorMapper;

    private final KpStationMapper stationMapper;

    /**
     * 查询充电设备管理
     *
     * @param id 主键
     * @return 充电设备管理
     */
    @Override
    public KpEquipmentVo queryById(Long id) {
        KpEquipmentVo vo = baseMapper.selectVoById(id);
        return getKpEquipmentVo(vo);
    }

    @NotNull
    private KpEquipmentVo getKpEquipmentVo(KpEquipmentVo vo) {
        KpOperatorVo kpOperatorVo = operatorMapper.selectVoById(vo.getOperatorId());
        KpStationVo kpStationVo = stationMapper.selectVoById(vo.getStationId());
        vo.setOperatorName(kpOperatorVo.getOperatorName());
        vo.setStationName(kpStationVo.getStationName());
        return vo;
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
        result.getRecords().forEach(this::getKpEquipmentVo);
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
        List<KpEquipmentVo> vos = baseMapper.selectVoList(lqw);
        vos.forEach(this::getKpEquipmentVo);
        return vos;
    }

    private LambdaQueryWrapper<KpEquipment> buildQueryWrapper(KpEquipmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpEquipment> lqw = Wrappers.lambdaQuery();
        lqw.eq(Objects.nonNull(bo.getStationId()), KpEquipment::getStationId, bo.getStationId());
        lqw.like(StringUtils.isNotBlank(bo.getEquipmentNo()), KpEquipment::getEquipmentNo, bo.getEquipmentNo());
        lqw.eq(Objects.nonNull(bo.getEquipmentType()), KpEquipment::getEquipmentType, bo.getEquipmentType());
        return lqw;
    }

    /**
     * 新增充电设备管理
     * todo  新增的同时 根据枪数 新增connector数据
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
            //如果新增成功 则开始新增归属枪
            KpConnectorBo kpConnectorBo = new KpConnectorBo();
            kpConnectorBo.setStationId(add.getStationId());
            kpConnectorBo.setOperatorId(add.getOperatorId());
            kpConnectorBo.setEquipmentId(add.getId());
            kpConnectorBo.setEquipmentNo(add.getEquipmentNo());
            for (int i = 1; i <= bo.getGunSum(); i++) {
                kpConnectorBo.setConnectorName(StringUtils.leftPad(i + "", 2, "0"));
                kpConnectorBo.setConnectorNo(i);
                KpConnector kpConnector = MapstructUtils.convert(kpConnectorBo, KpConnector.class);
                connectorMapper.insert(kpConnector);
            }
        }
        return flag;
    }

    /**
     * 修改充电设备管理
     * todo  修改的同时 修改绑定的connector数据
     *
     * @param bo 充电设备管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpEquipmentBo bo) {
        KpEquipment update = MapstructUtils.convert(bo, KpEquipment.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            KpConnectorBo kpConnectorBo = new KpConnectorBo();
            kpConnectorBo.setEquipmentId(update.getId());
            kpConnectorBo.setStationId(update.getStationId());
            kpConnectorBo.setOperatorId(update.getOperatorId());
            return connectorMapper.update(Wrappers.<KpConnector>lambdaUpdate()
                .eq(KpConnector::getEquipmentId, kpConnectorBo.getEquipmentId())
                .set(Objects.nonNull(kpConnectorBo.getStationId()), KpConnector::getStationId, kpConnectorBo.getStationId())
                .set(Objects.nonNull(kpConnectorBo.getOperatorId()), KpConnector::getOperatorId, kpConnectorBo.getOperatorId())
                .set(BaseEntity::getUpdateTime, DateUtil.date())) > 0;
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpEquipment entity) {
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
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }


    @Override
    public KpEquipment queryByEquipmentNo(String pileCode) {
        return baseMapper.selectOne(Wrappers.<KpEquipment>lambdaQuery()
            .eq(KpEquipment::getEquipmentNo, pileCode)
        );
    }

    @Override
    public void update(KpEquipment equipment) {
        baseMapper.updateById(equipment);
    }


    @Override
    public void pileLost(String pileCode) {
        baseMapper.update(Wrappers.<KpEquipment>lambdaUpdate()
            .eq(KpEquipment::getEquipmentNo, pileCode)
            .eq(KpEquipment::getDelFlag, 0)
            .set(KpEquipment::getIsWorking, 1)
            .set(KpEquipment::getNetType, 3)
            .set(BaseEntity::getUpdateTime, DateUtil.date())
        );
        connectorMapper.update(Wrappers.<KpConnector>lambdaUpdate()
            .eq(KpConnector::getEquipmentNo, pileCode)
            .eq(KpConnector::getDelFlag, 0)
            .set(KpConnector::getStatus, 0)
            .set(BaseEntity::getUpdateTime, DateUtil.date()));
    }
}
