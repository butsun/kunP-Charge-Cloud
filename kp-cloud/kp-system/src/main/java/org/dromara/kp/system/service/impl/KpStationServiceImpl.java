package org.dromara.kp.system.service.impl;

import org.dromara.common.core.exception.base.BaseException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.kp.system.domain.KpConnector;
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.domain.KpUserCar;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.vo.KpPriceTemplateVo;
import org.dromara.kp.system.mapper.KpEquipmentMapper;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.dromara.kp.system.mapper.KpPriceTemplateMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpStationBo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.domain.KpStation;
import org.dromara.kp.system.mapper.KpStationMapper;
import org.dromara.kp.system.service.IKpStationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;

/**
 * 站点管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpStationServiceImpl implements IKpStationService {

    private final KpStationMapper baseMapper;
    private final KpEquipmentMapper equipmentMapper;
    private final KpPriceTemplateMapper priceTemplateMapper;
    private final KpOperatorMapper operatorMapper;

    /**
     * 查询站点管理
     *
     * @param id 主键
     * @return 站点管理
     */
    @Override
    public KpStationVo queryById(Long id) {
        KpStationVo vo = baseMapper.selectVoById(id);
        return getKpEquipmentVo(vo);
    }


    @NotNull
    private KpStationVo getKpEquipmentVo(KpStationVo vo) {
        KpOperatorVo kpOperatorVo = operatorMapper.selectVoById(vo.getOperatorId());
        vo.setOperatorName(kpOperatorVo.getOperatorName());
        KpPriceTemplateVo kpPriceTemplateVo = priceTemplateMapper.selectVoById(vo.getPriceId());
        vo.setPriceTemplateName(kpPriceTemplateVo == null ? "" : kpPriceTemplateVo.getPriceName());
        return vo;
    }


    /**
     * 分页查询站点管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 站点管理分页列表
     */
    @Override
    public TableDataInfo<KpStationVo> queryPageList(KpStationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpStation> lqw = buildQueryWrapper(bo);
        Page<KpStationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::getKpEquipmentVo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的站点管理列表
     *
     * @param bo 查询条件
     * @return 站点管理列表
     */
    @Override
    public List<KpStationVo> queryList(KpStationBo bo) {
        LambdaQueryWrapper<KpStation> lqw = buildQueryWrapper(bo);
        List<KpStationVo> vos = baseMapper.selectVoList(lqw);
        vos.forEach(this::getKpEquipmentVo);
        return vos;
    }

    private LambdaQueryWrapper<KpStation> buildQueryWrapper(KpStationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpStation> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOperatorId()), KpStation::getOperatorId, bo.getOperatorId());
        lqw.like(StringUtils.isNotBlank(bo.getStationName()), KpStation::getStationName, bo.getStationName());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), KpStation::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), KpStation::getCity, bo.getCity());
        lqw.eq(bo.getStationType() != null, KpStation::getStationType, bo.getStationType());
        lqw.eq(bo.getStationStatus() != null, KpStation::getStationStatus, bo.getStationStatus());
        lqw.eq(KpStation::getDelFlag, 0);
        lqw.eq(bo.getPriceId() != null, KpStation::getPriceId, bo.getPriceId());
        return lqw;
    }

    /**
     * 新增站点管理
     *
     * @param bo 站点管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpStationBo bo) {
        KpStation add = MapstructUtils.convert(bo, KpStation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改站点管理
     *
     * @param bo 站点管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpStationBo bo) {
        KpStation update = MapstructUtils.convert(bo, KpStation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpStation entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除站点管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Optional.ofNullable(equipmentMapper.selectOne(Wrappers.lambdaQuery(KpEquipment.class)
                .in(KpEquipment::getStationId, ids)
                .eq(KpEquipment::getDelFlag, 0), false)).ifPresent(kpStation -> {
                throw new BaseException("删除站点还存在可用设备，请先删除设备");
            });
        }
        return baseMapper.update(Wrappers.lambdaUpdate(KpStation.class)
            .in(KpStation::getId, ids)
            .set(KpStation::getDelFlag, 1)
        ) > 0;
    }


    @Override
    public List<Long> getLinkStations(Long priceCode) {
        LambdaQueryWrapper<KpStation> lqw = Wrappers.<KpStation>lambdaQuery().eq(KpStation::getPriceId, priceCode)
            .eq(KpStation::getDelFlag, 0)
            .select(KpStation::getId);
        List<KpStationVo> kpStationVos = baseMapper.selectVoList(lqw);
        return kpStationVos.stream().map(KpStationVo::getId).toList();
    }
}
