package org.dromara.kp.system.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.kp.system.domain.KpChargeVoucher;
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpDiscountActivityBo;
import org.dromara.kp.system.domain.vo.KpDiscountActivityVo;
import org.dromara.kp.system.domain.KpDiscountActivity;
import org.dromara.kp.system.mapper.KpDiscountActivityMapper;
import org.dromara.kp.system.service.IKpDiscountActivityService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;

/**
 * 充电优惠管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpDiscountActivityServiceImpl implements IKpDiscountActivityService {

    private final KpDiscountActivityMapper baseMapper;

    private final KpOperatorMapper operatorMapper;
    /**
     * 查询充电优惠管理
     *
     * @param id 主键
     * @return 充电优惠管理
     */
    @Override
    public KpDiscountActivityVo queryById(Long id){
        KpDiscountActivityVo vo = baseMapper.selectVoById(id);
        return getKpDiscountActivityVo(vo);
    }

    @NotNull
    private KpDiscountActivityVo getKpDiscountActivityVo(KpDiscountActivityVo vo) {
        KpOperatorVo kpOperatorVo = operatorMapper.selectVoById(vo.getOperatorId());
        vo.setOperatorName(kpOperatorVo.getOperatorName());
        return vo;
    }

    /**
     * 分页查询充电优惠管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电优惠管理分页列表
     */
    @Override
    public TableDataInfo<KpDiscountActivityVo> queryPageList(KpDiscountActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpDiscountActivity> lqw = buildQueryWrapper(bo);
        Page<KpDiscountActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::getKpDiscountActivityVo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的充电优惠管理列表
     *
     * @param bo 查询条件
     * @return 充电优惠管理列表
     */
    @Override
    public List<KpDiscountActivityVo> queryList(KpDiscountActivityBo bo) {
        LambdaQueryWrapper<KpDiscountActivity> lqw = buildQueryWrapper(bo);
        List<KpDiscountActivityVo> vos = baseMapper.selectVoList(lqw);
        vos.forEach(this::getKpDiscountActivityVo);
        return vos;
    }

    private LambdaQueryWrapper<KpDiscountActivity> buildQueryWrapper(KpDiscountActivityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpDiscountActivity> lqw = Wrappers.lambdaQuery();
        lqw.like(Objects.nonNull(bo.getActivityName()), KpDiscountActivity::getActivityName, bo.getActivityName());
        lqw.eq(Objects.nonNull(bo.getOperatorId()), KpDiscountActivity::getOperatorId, bo.getOperatorId());
        lqw.eq(Objects.nonNull(bo.getStationId()), KpDiscountActivity::getStationId, bo.getStationId());
        lqw.eq(bo.getDisService() != null, KpDiscountActivity::getDisService, bo.getDisService());
        lqw.eq(bo.getDisElectricity() != null, KpDiscountActivity::getDisElectricity, bo.getDisElectricity());
        lqw.eq(KpDiscountActivity::getDelFlag, 0);

        return lqw;
    }

    /**
     * 新增充电优惠管理
     *
     * @param bo 充电优惠管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpDiscountActivityBo bo) {
        KpDiscountActivity add = MapstructUtils.convert(bo, KpDiscountActivity.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电优惠管理
     *
     * @param bo 充电优惠管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpDiscountActivityBo bo) {
        KpDiscountActivity update = MapstructUtils.convert(bo, KpDiscountActivity.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpDiscountActivity entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除充电优惠管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
        }
        return baseMapper.update(Wrappers.lambdaUpdate(KpDiscountActivity.class)
            .in(KpDiscountActivity::getId, ids)
            .set(KpDiscountActivity::getDelFlag, 1)
        ) > 0;
    }

    @Override
    public KpDiscountActivity queryByOperatorId(Long operatorId ,Integer accountType) {
        return baseMapper.selectOne(Wrappers.lambdaQuery(KpDiscountActivity.class)
            .eq(KpDiscountActivity::getOperatorId, operatorId)
            .eq(KpDiscountActivity::getDisableFlag,0)
            .eq(KpDiscountActivity::getDelFlag,0)
            .eq(KpDiscountActivity::getActivityType,accountType)
        );
    }
}
