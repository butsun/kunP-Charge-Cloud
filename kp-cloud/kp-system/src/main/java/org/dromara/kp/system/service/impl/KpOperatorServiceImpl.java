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
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.kp.system.domain.KpStation;
import org.dromara.kp.system.mapper.KpStationMapper;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpOperatorBo;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.KpOperator;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.dromara.kp.system.service.IKpOperatorService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;

/**
 * 运营商管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpOperatorServiceImpl implements IKpOperatorService {

    private final KpOperatorMapper baseMapper;
    private final KpStationMapper stationMapper;

    /**
     * 查询运营商管理
     *
     * @param id 主键
     * @return 运营商管理
     */
    @Override
    public KpOperatorVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询运营商管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 运营商管理分页列表
     */
    @Override
    public TableDataInfo<KpOperatorVo> queryPageList(KpOperatorBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpOperator> lqw = buildQueryWrapper(bo);
        Page<KpOperatorVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的运营商管理列表
     *
     * @param bo 查询条件
     * @return 运营商管理列表
     */
    @Override
    public List<KpOperatorVo> queryList(KpOperatorBo bo) {
        LambdaQueryWrapper<KpOperator> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KpOperator> buildQueryWrapper(KpOperatorBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpOperator> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), KpOperator::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), KpOperator::getCity, bo.getCity());
        lqw.like(StringUtils.isNotBlank(bo.getOperatorName()), KpOperator::getOperatorName, bo.getOperatorName());
        lqw.eq(KpOperator::getDelFlag, 0);

        return lqw;
    }

    /**
     * 新增运营商管理
     *
     * @param bo 运营商管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpOperatorBo bo) {
        KpOperator add = MapstructUtils.convert(bo, KpOperator.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改运营商管理
     *
     * @param bo 运营商管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpOperatorBo bo) {
        KpOperator update = MapstructUtils.convert(bo, KpOperator.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpOperator entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除运营商管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Optional.ofNullable(stationMapper.selectOne(Wrappers.lambdaQuery(KpStation.class)
                .in(KpStation::getOperatorId, ids)
                .eq(KpStation::getDelFlag, 0),false)).ifPresent(kpStation -> {
                throw new BaseException("删除运营商还存在运营站点，请先删除站点");
            });
        }
        return baseMapper.update(Wrappers.lambdaUpdate(KpOperator.class)
            .in(KpOperator::getId, ids)
            .set(KpOperator::getDelFlag, 1)
        ) > 0;
    }
}
