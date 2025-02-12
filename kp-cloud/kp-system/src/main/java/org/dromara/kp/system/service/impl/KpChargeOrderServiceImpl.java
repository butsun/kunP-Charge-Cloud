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
import org.dromara.kp.system.domain.KpOperator;
import org.dromara.kp.system.domain.KpStation;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.dromara.kp.system.mapper.KpStationMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpChargeOrderBo;
import org.dromara.kp.system.domain.vo.KpChargeOrderVo;
import org.dromara.kp.system.domain.KpChargeOrder;
import org.dromara.kp.system.mapper.KpChargeOrderMapper;
import org.dromara.kp.system.service.IKpChargeOrderService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;

/**
 * 充电订单管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpChargeOrderServiceImpl implements IKpChargeOrderService {

    private final KpChargeOrderMapper baseMapper;
    private final KpOperatorMapper operatorMapper;
    private final KpStationMapper stationMapper;
    /**
     * 查询充电订单管理
     *
     * @param id 主键
     * @return 充电订单管理
     */
    @Override
    public KpChargeOrderVo queryById(Long id) {
        KpChargeOrderVo vo = baseMapper.selectVoById(id);
        return getKpChargeOrderVo(vo);
    }

    @NotNull
    private KpChargeOrderVo getKpChargeOrderVo(KpChargeOrderVo vo) {
        KpOperator kpOperatorVo = operatorMapper.selectById(vo.getOperatorId());
        KpStation kpStationVo = stationMapper.selectById(vo.getStationId());
        vo.setOperatorName(kpOperatorVo.getOperatorName());
        vo.setStationName(kpStationVo.getStationName());
        vo.setActivityMoney( vo.getTotalMoney().subtract(vo.getFinalTotalMoney()) );
        return vo;
    }


    /**
     * 分页查询充电订单管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电订单管理分页列表
     */
    @Override
    public TableDataInfo<KpChargeOrderVo> queryPageList(KpChargeOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpChargeOrder> lqw = buildQueryWrapper(bo);
        Page<KpChargeOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::getKpChargeOrderVo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的充电订单管理列表
     *
     * @param bo 查询条件
     * @return 充电订单管理列表
     */
    @Override
    public List<KpChargeOrderVo> queryList(KpChargeOrderBo bo) {
        LambdaQueryWrapper<KpChargeOrder> lqw = buildQueryWrapper(bo);
        List<KpChargeOrderVo> vos = baseMapper.selectVoList(lqw);
        vos.forEach(this::getKpChargeOrderVo);
        return vos;
    }

    private LambdaQueryWrapper<KpChargeOrder> buildQueryWrapper(KpChargeOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpChargeOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getStartChargeSeq()), KpChargeOrder::getStartChargeSeq, bo.getStartChargeSeq());
        lqw.eq(Objects.nonNull(bo.getOperatorId()), KpChargeOrder::getOperatorId, bo.getOperatorId());
        lqw.eq(bo.getStartChargeSeqStat() != null, KpChargeOrder::getStartChargeSeqStat, bo.getStartChargeSeqStat());
        lqw.eq(Objects.nonNull(bo.getStationId()), KpChargeOrder::getStationId, bo.getStationId());
        lqw.between(params.get("beginStartTime") != null && params.get("endStartTime") != null,
            KpChargeOrder::getStartTime, params.get("beginStartTime"), params.get("endStartTime"));
        lqw.eq(KpChargeOrder::getDelFlag, 0);

        return lqw;
    }

    /**
     * 新增充电订单管理
     *
     * @param bo 充电订单管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpChargeOrderBo bo) {
        KpChargeOrder add = MapstructUtils.convert(bo, KpChargeOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电订单管理
     *
     * @param bo 充电订单管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpChargeOrderBo bo) {
        KpChargeOrder update = MapstructUtils.convert(bo, KpChargeOrder.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpChargeOrder entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除充电订单管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO  不允许删除
        }
        return baseMapper.deleteByIds(ids) > 0;
    }


    @Override
    public KpChargeOrder queryByTradeNo(String tradeNo, String pileCode, int gunCode) {
        return baseMapper.selectOne(Wrappers.<KpChargeOrder>lambdaQuery()
            .eq(KpChargeOrder::getTradeNo, tradeNo)
            .eq(KpChargeOrder::getEquipmentNo, pileCode)
            .eq(KpChargeOrder::getConnectorNo, gunCode)
        );
    }


    @Override
    public boolean insertOrder(KpChargeOrder kpChargeOrder) {
        return baseMapper.insert(kpChargeOrder) > 0;
    }


    @Override
    public boolean refreshOrder(KpChargeOrder chargeOrder) {
        return baseMapper.updateById(chargeOrder) > 0;
    }
}
