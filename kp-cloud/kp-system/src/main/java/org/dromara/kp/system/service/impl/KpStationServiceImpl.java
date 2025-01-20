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
import org.dromara.kp.system.domain.bo.KpStationBo;
import org.dromara.kp.system.domain.vo.KpStationVo;
import org.dromara.kp.system.domain.KpStation;
import org.dromara.kp.system.mapper.KpStationMapper;
import org.dromara.kp.system.service.IKpStationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

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

    /**
     * 查询站点管理
     *
     * @param id 主键
     * @return 站点管理
     */
    @Override
    public KpStationVo queryById(Long id){
        return baseMapper.selectVoById(id);
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
        return baseMapper.selectVoList(lqw);
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
    private void validEntityBeforeSave(KpStation entity){
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
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
