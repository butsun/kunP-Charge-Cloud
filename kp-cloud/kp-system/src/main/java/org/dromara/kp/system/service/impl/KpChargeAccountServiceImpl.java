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
import org.dromara.kp.system.domain.KpChargeVoucher;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.domain.vo.KpOperatorVo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.mapper.KpChargeVoucherMapper;
import org.dromara.kp.system.mapper.KpOperatorMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpChargeAccountBo;
import org.dromara.kp.system.domain.vo.KpChargeAccountVo;
import org.dromara.kp.system.domain.KpChargeAccount;
import org.dromara.kp.system.mapper.KpChargeAccountMapper;
import org.dromara.kp.system.service.IKpChargeAccountService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;

/**
 * 充电账户Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpChargeAccountServiceImpl implements IKpChargeAccountService {

    private final KpChargeAccountMapper baseMapper;

    private final KpOperatorMapper operatorMapper;

    private final KpChargeVoucherMapper chargeVoucherMapper;

    /**
     * 查询充电账户
     *
     * @param id 主键
     * @return 充电账户
     */
    @Override
    public KpChargeAccountVo queryById(Long id) {
        KpChargeAccountVo vo = baseMapper.selectVoById(id);
        return getChargeAccountVo(vo);
    }


    @NotNull
    private KpChargeAccountVo getChargeAccountVo(KpChargeAccountVo vo) {
        KpOperatorVo kpOperatorVo = operatorMapper.selectVoById(vo.getOperatorId());
        vo.setOperatorName(kpOperatorVo.getOperatorName());
        return vo;
    }


    /**
     * 分页查询充电账户列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 充电账户分页列表
     */
    @Override
    public TableDataInfo<KpChargeAccountVo> queryPageList(KpChargeAccountBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpChargeAccount> lqw = buildQueryWrapper(bo);
        Page<KpChargeAccountVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::getChargeAccountVo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的充电账户列表
     *
     * @param bo 查询条件
     * @return 充电账户列表
     */
    @Override
    public List<KpChargeAccountVo> queryList(KpChargeAccountBo bo) {
        LambdaQueryWrapper<KpChargeAccount> lqw = buildQueryWrapper(bo);
        List<KpChargeAccountVo> vos = baseMapper.selectVoList(lqw);
        vos.forEach(this::getChargeAccountVo);
        return vos;
    }

    private LambdaQueryWrapper<KpChargeAccount> buildQueryWrapper(KpChargeAccountBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpChargeAccount> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getMobile()), KpChargeAccount::getMobile, bo.getMobile());
        lqw.like(StringUtils.isNotBlank(bo.getNickName()), KpChargeAccount::getNickName, bo.getNickName());
        lqw.eq(bo.getAccoutType() != null, KpChargeAccount::getAccoutType, bo.getAccoutType());
        lqw.eq( KpChargeAccount::getDelFlag,0);
        return lqw;
    }

    /**
     * 新增充电账户
     *
     * @param bo 充电账户
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpChargeAccountBo bo) {
        KpChargeAccount add = MapstructUtils.convert(bo, KpChargeAccount.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电账户
     *
     * @param bo 充电账户
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpChargeAccountBo bo) {
        KpChargeAccount update = MapstructUtils.convert(bo, KpChargeAccount.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpChargeAccount entity) {
        LambdaQueryWrapper<KpChargeAccount> lqw = buildValidQuery(entity);
        KpChargeAccount kpChargeAccount = baseMapper.selectOne(lqw);
        if (kpChargeAccount != null) {
            throw new BaseException("该手机号账户已经存在");
        }
    }

    private LambdaQueryWrapper<KpChargeAccount> buildValidQuery(KpChargeAccount entity) {
        LambdaQueryWrapper<KpChargeAccount> lqw = Wrappers.lambdaQuery();
        lqw.eq(KpChargeAccount::getMobile, entity.getMobile());
        lqw.eq(KpChargeAccount::getAccoutType, entity.getAccoutType());
        lqw.eq(KpChargeAccount::getDisableFlag, 0);
        return lqw;
    }

    /**
     * 校验并批量删除充电账户信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Optional.ofNullable(chargeVoucherMapper.selectOne(Wrappers.lambdaQuery(KpChargeVoucher.class)
                .in(KpChargeVoucher::getAccountId, ids)
                .eq(KpChargeVoucher::getDelFlag, 0),false
            )).ifPresent(kpChargeVoucher -> {
                throw new BaseException("删除账户还存在有效凭证，请先删除凭证");
            });
        }
        return baseMapper.update(Wrappers.lambdaUpdate(KpChargeAccount.class)
            .in(KpChargeAccount::getId, ids)
            .set(KpChargeAccount::getDelFlag, 1)
        ) > 0;
    }


}
