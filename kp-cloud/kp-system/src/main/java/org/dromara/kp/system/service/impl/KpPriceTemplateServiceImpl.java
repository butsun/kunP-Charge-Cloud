package org.dromara.kp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.kp.system.domain.bo.KpPriceTemplateBo;
import org.dromara.kp.system.domain.resposne.PriceInfoResponse;
import org.dromara.kp.system.domain.vo.KpPriceTemplateVo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.mapper.KpPriceTemplateMapper;
import org.dromara.kp.system.mapper.KpStationMapper;
import org.dromara.kp.system.service.IKpPriceTemplateService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 站点价格模版Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpPriceTemplateServiceImpl implements IKpPriceTemplateService {

    private final KpPriceTemplateMapper baseMapper;
    private final KpStationMapper stationMapper;

    /**
     * 查询价格模版管理
     *
     * @param id 主键
     * @return 价格模版管理
     */
    @Override
    public KpPriceTemplateVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询价格模版管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 价格模版管理分页列表
     */
    @Override
    public TableDataInfo<KpPriceTemplateVo> queryPageList(KpPriceTemplateBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpPriceTemplate> lqw = buildQueryWrapper(bo);
        Page<KpPriceTemplateVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的价格模版管理列表
     *
     * @param bo 查询条件
     * @return 价格模版管理列表
     */
    @Override
    public List<KpPriceTemplateVo> queryList(KpPriceTemplateBo bo) {
        LambdaQueryWrapper<KpPriceTemplate> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KpPriceTemplate> buildQueryWrapper(KpPriceTemplateBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpPriceTemplate> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getPriceName()), KpPriceTemplate::getPriceName, bo.getPriceName());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), KpPriceTemplate::getRemark, bo.getRemark());
        return lqw;
    }

    /**
     * 新增价格模版管理
     *
     * @param bo 价格模版管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpPriceTemplateBo bo) {
        KpPriceTemplate add = MapstructUtils.convert(bo, KpPriceTemplate.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改价格模版管理
     *
     * @param bo 价格模版管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpPriceTemplateBo bo) {
        KpPriceTemplate update = MapstructUtils.convert(bo, KpPriceTemplate.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpPriceTemplate entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除价格模版管理信息
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
    public KpPriceTemplate queryByStationId(Long stationId) {
        KpStationVo kpStationVo = stationMapper.selectVoById(stationId);
        if (kpStationVo != null) {
            LambdaQueryWrapper<KpPriceTemplate> lqw = Wrappers.lambdaQuery();
            lqw.eq(KpPriceTemplate::getId, kpStationVo.getPriceId());
            return baseMapper.selectOne(lqw);
        }
        return null;
    }
}
