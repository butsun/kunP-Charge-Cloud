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
import org.dromara.kp.system.domain.bo.KpUserCarBo;
import org.dromara.kp.system.domain.vo.KpUserCarVo;
import org.dromara.kp.system.domain.KpUserCar;
import org.dromara.kp.system.mapper.KpUserCarMapper;
import org.dromara.kp.system.service.IKpUserCarService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 车辆管理Service业务层处理
 *
 * @author LionLi
 * @date 2025-01-20
 */
@RequiredArgsConstructor
@Service
public class KpUserCarServiceImpl implements IKpUserCarService {

    private final KpUserCarMapper baseMapper;

    /**
     * 查询车辆管理
     *
     * @param id 主键
     * @return 车辆管理
     */
    @Override
    public KpUserCarVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询车辆管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 车辆管理分页列表
     */
    @Override
    public TableDataInfo<KpUserCarVo> queryPageList(KpUserCarBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpUserCar> lqw = buildQueryWrapper(bo);
        Page<KpUserCarVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的车辆管理列表
     *
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    @Override
    public List<KpUserCarVo> queryList(KpUserCarBo bo) {
        LambdaQueryWrapper<KpUserCar> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KpUserCar> buildQueryWrapper(KpUserCarBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KpUserCar> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAccountId() != null, KpUserCar::getAccountId, bo.getAccountId());
        lqw.eq(StringUtils.isNotBlank(bo.getPlateNo()), KpUserCar::getPlateNo, bo.getPlateNo());
        lqw.eq(StringUtils.isNotBlank(bo.getCarVin()), KpUserCar::getCarVin, bo.getCarVin());
        return lqw;
    }

    /**
     * 新增车辆管理
     *
     * @param bo 车辆管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(KpUserCarBo bo) {
        KpUserCar add = MapstructUtils.convert(bo, KpUserCar.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改车辆管理
     *
     * @param bo 车辆管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(KpUserCarBo bo) {
        KpUserCar update = MapstructUtils.convert(bo, KpUserCar.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KpUserCar entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除车辆管理信息
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
