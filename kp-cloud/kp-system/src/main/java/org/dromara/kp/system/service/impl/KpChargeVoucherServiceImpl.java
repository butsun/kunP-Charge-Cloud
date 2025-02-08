package org.dromara.kp.system.service.impl;

import org.bouncycastle.jcajce.provider.symmetric.TEA;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.kp.system.domain.vo.*;
import org.dromara.kp.system.mapper.KpChargeAccountMapper;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpChargeVoucherBo;
import org.dromara.kp.system.domain.KpChargeVoucher;
import org.dromara.kp.system.mapper.KpChargeVoucherMapper;
import org.dromara.kp.system.service.IKpChargeVoucherService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;

/**
 * 充电凭证管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpChargeVoucherServiceImpl implements IKpChargeVoucherService {

    private final KpChargeVoucherMapper baseMapper;

    private final KpOperatorMapper operatorMapper;

    private final KpChargeAccountMapper chargeAccountMapper;

    /**
     * 查询充电凭证管理
     *
     * @param id 主键
     * @return 充电凭证管理
     */
    @Override
    public KpChargeVoucherVo queryById(Long id) {
        KpChargeVoucherVo vo = baseMapper.selectVoById(id);
        return getKpChargeVoucherVo(vo);
    }


    @NotNull
    private KpChargeVoucherVo getKpChargeVoucherVo(KpChargeVoucherVo vo) {
        KpOperatorVo kpOperatorVo = operatorMapper.selectVoById(vo.getOperatorId());
        KpChargeAccountVo kpChargeAccountVo = chargeAccountMapper.selectVoById(vo.getAccountId());
        vo.setOperatorName(kpOperatorVo.getOperatorName());
        vo.setMobile(kpChargeAccountVo.getMobile());
        vo.setNickName(kpChargeAccountVo.getNickName());
        return vo;
    }

    /**
     * 分页查询充电凭证管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电凭证管理分页列表
     */
    @Override
    public TableDataInfo<KpChargeVoucherVo> queryPageList(KpChargeVoucherBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpChargeVoucher> lqw = buildQueryWrapper(bo);
        Page<KpChargeVoucherVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::getKpChargeVoucherVo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的充电凭证管理列表
     *
     * @param bo 查询条件
     * @return 充电凭证管理列表
     */
    @Override
    public List<KpChargeVoucherVo> queryList(KpChargeVoucherBo bo) {
        LambdaQueryWrapper<KpChargeVoucher> lqw = buildQueryWrapper(bo);
        List<KpChargeVoucherVo> vos = baseMapper.selectVoList(lqw);
        vos.forEach(this::getKpChargeVoucherVo);
        return vos;
    }

    private LambdaQueryWrapper<KpChargeVoucher> buildQueryWrapper(KpChargeVoucherBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpChargeVoucher> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getVoucherNumber()), KpChargeVoucher::getVoucherNumber, bo.getVoucherNumber());
        lqw.eq(Objects.nonNull(bo.getOperatorId()), KpChargeVoucher::getOperatorId, bo.getOperatorId());
        lqw.eq(bo.getDisableFlag() != null, KpChargeVoucher::getDisableFlag, bo.getDisableFlag());
        return lqw;
    }

    /**
     * 新增充电凭证管理
     *
     * @param bo 充电凭证管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpChargeVoucherBo bo) {
        KpChargeVoucher add = MapstructUtils.convert(bo, KpChargeVoucher.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电凭证管理
     *
     * @param bo 充电凭证管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpChargeVoucherBo bo) {
        KpChargeVoucher update = MapstructUtils.convert(bo, KpChargeVoucher.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpChargeVoucher entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除充电凭证管理信息
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
}
