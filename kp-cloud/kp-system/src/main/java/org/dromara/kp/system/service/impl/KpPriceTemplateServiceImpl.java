package org.dromara.kp.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.http.util.TextUtils;
import org.dromara.common.core.exception.base.BaseException;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.kp.system.domain.request.PriceAddRequest;
import org.dromara.kp.system.domain.resposne.PriceInfoResponse;
import org.dromara.kp.system.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.dromara.kp.system.domain.bo.KpPriceTemplateBo;
import org.dromara.kp.system.domain.KpPriceTemplate;
import org.dromara.kp.system.mapper.KpPriceTemplateMapper;
import org.dromara.kp.system.service.IKpPriceTemplateService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.baomidou.mybatisplus.extension.toolkit.Db.saveBatch;

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

    private final KpStationServiceImpl kpStationService;


    @Override
    public TableDataInfo<PriceListDto> getPricePage(KpPriceTemplateBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KpPriceTemplate> queryWrapper = buildQueryWrapper(bo);
        Page<KpPriceTemplateVo> page = baseMapper.selectVoPage(pageQuery.build(), queryWrapper);
        List<PriceListDto> priceListDtoList = new ArrayList<>();
        for (KpPriceTemplateVo vo : page.getRecords()) {
            PriceListDto priceListDto = new PriceListDto();
            priceListDto.setPriceCode(vo.getPriceCode());
            priceListDto.setRemark(vo.getRemark());
            if (vo.getUpdateTime() != null) {
                priceListDto.setTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, vo.getUpdateTime()));
            }
            priceListDtoList.add(priceListDto);
        }
        Page<PriceListDto> newPage = new Page<>();
        newPage.setRecords(priceListDtoList);
        newPage.setSize(page.getSize());
        newPage.setCurrent(page.getCurrent());
        newPage.setTotal(page.getTotal());
        return TableDataInfo.build(newPage);
    }


    @Override
    public PriceInfoResponse getPriceInfo(Long priceCode) {
        PriceInfoResponse priceInfoResponse = new PriceInfoResponse();
        LambdaQueryWrapper<KpPriceTemplate> qw = Wrappers.<KpPriceTemplate>lambdaQuery().eq(KpPriceTemplate::getPriceCode, priceCode).orderByAsc(KpPriceTemplate::getStartTime);
        List<KpPriceTemplateVo> list = baseMapper.selectVoList(qw);
        if (list == null || list.isEmpty() && priceCode != 0) {
            return getPriceInfo(0L);
        }
        List<PriceInfoData> priceInfoDataList = new ArrayList<>();
        List<PriceTypeInfoData> priceTypeInfoDataList = new ArrayList<>();
        PriceTypeInfoData type0 = new PriceTypeInfoData((short) 0);
        PriceTypeInfoData type1 = new PriceTypeInfoData((short) 1);
        PriceTypeInfoData type2 = new PriceTypeInfoData((short) 2);
        PriceTypeInfoData type3 = new PriceTypeInfoData((short) 3);
        priceTypeInfoDataList.add(type0);
        priceTypeInfoDataList.add(type1);
        priceTypeInfoDataList.add(type2);
        priceTypeInfoDataList.add(type3);

        for (KpPriceTemplateVo vo : list) {
            if (!TextUtils.isBlank(vo.getRemark()) && TextUtils.isBlank(priceInfoResponse.getRemark())) {
                priceInfoResponse.setRemark(vo.getRemark());
            }
            PriceInfoData data = new PriceInfoData();
            data.setStartHour(vo.getStartTime());
            data.setPriceType(vo.getPriceType());
            data.setElecPrice(vo.getElecPrice());
            data.setServicePrice(vo.getServicePrice());
            priceInfoDataList.add(data);

            if (vo.getPriceType() == 0) {
                type0.setElecPrice(vo.getElecPrice());
                type0.setServicePrice(vo.getServicePrice());
            } else if (vo.getPriceType() == 1) {
                type1.setElecPrice(vo.getElecPrice());
                type1.setServicePrice(vo.getServicePrice());
            } else if (vo.getPriceType() == 2) {
                type2.setElecPrice(vo.getElecPrice());
                type2.setServicePrice(vo.getServicePrice());
            } else if (vo.getPriceType() == 3) {
                type3.setElecPrice(vo.getElecPrice());
                type3.setServicePrice(vo.getServicePrice());
            }
        }
        priceInfoResponse.setPriceCode(priceCode);
        priceInfoResponse.setPriceList(priceInfoDataList);
        priceInfoResponse.setPriceTypeList(priceTypeInfoDataList);
        return priceInfoResponse;
    }

    @Override
    public PriceInfoResponse getStationPriceInfo(Long stationId) {
        KpStationVo kpStationVo = kpStationService.queryById(stationId);
        Long priceCode = 0L;
        if (kpStationVo != null) {
            priceCode = kpStationVo.getPriceCode();
        }
        PriceInfoResponse priceInfoResponse = getPriceInfo(priceCode);
        priceInfoResponse.setStationId(stationId);
        return priceInfoResponse;
    }


    @Override
    public void removePrice(Long priceCode) {
        //首先检查有无关联station
        if (priceCode == 0) {
            throw new BaseException("默认模版无法删除");
        } else if (priceCode <= 0) {
            throw new BaseException("无效的价格编码");
        }

        List<Long> list = kpStationService.getLinkStations(priceCode);
        if (list != null && !list.isEmpty()) {
            throw new BaseException("不可删除，有" + list.size() + "个关联的站点。" + JsonUtils.toJsonString(list));
        }
        LambdaQueryWrapper<KpPriceTemplate> lqw = Wrappers.<KpPriceTemplate>lambdaQuery().eq(KpPriceTemplate::getPriceCode, priceCode);
        if (baseMapper.delete(lqw) < 1) {
            throw new BaseException("价格删除失败");
        }
    }

    private LambdaQueryWrapper<KpPriceTemplate> buildQueryWrapper(KpPriceTemplateBo bo) {
        LambdaQueryWrapper<KpPriceTemplate> lqw = Wrappers.lambdaQuery();
        lqw.eq(KpPriceTemplate::getMainPoint, 1);
        lqw.eq(bo.getPriceCode() != null, KpPriceTemplate::getPriceCode, bo.getPriceCode());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), KpPriceTemplate::getRemark, bo.getRemark());
        return lqw;
    }


    @Override
    public Long addPrice(PriceAddRequest priceAddRequest) {
        //数据校验
        validEntityBeforeSave(priceAddRequest.getPriceList());
        //锁
        Long maxPriceCode = getMaxPriceCode();
        List<KpPriceTemplate> priceList = new ArrayList<>();

        Date upTime = new Date();
        for (PriceInfoData priceInfoData : priceAddRequest.getPriceList()) {
            KpPriceTemplate kpPriceTemplate = new KpPriceTemplate();
            kpPriceTemplate.setStartTime(priceInfoData.getStartHour());
            kpPriceTemplate.setServicePrice(priceInfoData.getServicePrice());
            kpPriceTemplate.setElecPrice(priceInfoData.getElecPrice());
            kpPriceTemplate.setPriceType(priceInfoData.getPriceType());
            kpPriceTemplate.setPriceCode(maxPriceCode);
            if ("000000".equals(priceInfoData.getStartHour())) {
                kpPriceTemplate.setRemark(priceAddRequest.getRemark());
                kpPriceTemplate.setMainPoint((short) 1);
            }
            kpPriceTemplate.setUpdateTime(upTime);
            priceList.add(kpPriceTemplate);
        }

        if (!baseMapper.insertBatch(priceList)) {
            throw new BaseException("价格保存失败");
        }
        return maxPriceCode;
    }


    @Override
    public void editPrice(PriceEditRequest priceEditRequest) {
        List<PriceInfoData> priceList = priceEditRequest.getPriceList();
        validEntityBeforeSave(priceList);
        LambdaQueryWrapper<KpPriceTemplate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(KpPriceTemplate::getPriceCode, priceEditRequest.getPriceCode());
        queryWrapper.orderByAsc(KpPriceTemplate::getStartTime);
        List<KpPriceTemplate> kpPriceTemplates = baseMapper.selectList(queryWrapper);
        if (kpPriceTemplates == null || kpPriceTemplates.isEmpty()) {
            throw new BaseException("编辑价格模版不存在");
        }
        for (int i = 0; i < 48; i++) {
            KpPriceTemplate oldP = kpPriceTemplates.get(i);
            PriceInfoData newP = priceList.get(i);
            oldP.setElecPrice(newP.getElecPrice());
            oldP.setPriceType(newP.getPriceType());
            oldP.setServicePrice(newP.getServicePrice());
            if (oldP.getMainPoint() == 1) {
                oldP.setRemark(priceEditRequest.getRemark());
            }
        }
        baseMapper.updateBatchById(kpPriceTemplates);
    }


    @Override
    public Long getMaxPriceCode() {
        //todo add redis
        LambdaQueryWrapper<KpPriceTemplate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(KpPriceTemplate::getPriceCode);
        queryWrapper.orderByDesc(KpPriceTemplate::getPriceCode);
        queryWrapper.last("limit 1");
        KpPriceTemplateVo KpPriceTemplate = baseMapper.selectVoOne(queryWrapper);
        if (KpPriceTemplate == null) {
            return 1L;
        } else {
            return KpPriceTemplate.getPriceCode() + RandomUtil.randomLong(1, 5);
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(List<PriceInfoData> priceList) {
        if (priceList == null || priceList.size() != 48) {
            throw new BaseException("无效的价格");
        }
        BigDecimal t0s = new BigDecimal("-0.0001");
        BigDecimal t0e = new BigDecimal("-0.0001");
        BigDecimal t1s = new BigDecimal("-0.0001");
        BigDecimal t1e = new BigDecimal("-0.0001");
        BigDecimal t2s = new BigDecimal("-0.0001");
        BigDecimal t2e = new BigDecimal("-0.0001");
        BigDecimal t3s = new BigDecimal("-0.0001");
        BigDecimal t3e = new BigDecimal("-0.0001");

        for (PriceInfoData priceInfoData : priceList) {

            if (priceInfoData == null) {
                throw new BaseException("无效的价格对象");
            }
            if (!ArrayUtil.contains(startHours, priceInfoData.getStartHour())) {
                throw new BaseException("错误的开始时间");
            }
            if (priceInfoData.getPriceType() == null
                || priceInfoData.getPriceType() < 0
                || priceInfoData.getPriceType() > 3) {
                throw new BaseException("无效的价格类型");
            }
            if (priceInfoData.getElecPrice() == null || priceInfoData.getElecPrice().floatValue() < 0) {
                throw new BaseException("无效的电价");
            }
            if (priceInfoData.getServicePrice() == null || priceInfoData.getServicePrice().floatValue() < 0) {
                throw new BaseException("无效的服务费价格");
            }
            if (priceInfoData.getPriceType() == 0) {
                if (t0s.floatValue() >= 0) {
                    if (t0s.compareTo(priceInfoData.getServicePrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t0s = priceInfoData.getServicePrice().setScale(4, RoundingMode.HALF_EVEN);
                }

                if (t0e.floatValue() >= 0) {
                    if (t0e.compareTo(priceInfoData.getElecPrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t0e = priceInfoData.getElecPrice().setScale(4, RoundingMode.HALF_EVEN);
                }
            } else if (priceInfoData.getPriceType() == 1) {
                if (t1s.floatValue() >= 0) {
                    if (t1s.compareTo(priceInfoData.getServicePrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t1s = priceInfoData.getServicePrice().setScale(4, RoundingMode.HALF_EVEN);
                }

                if (t1e.floatValue() >= 0) {
                    if (t1e.compareTo(priceInfoData.getElecPrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t1e = priceInfoData.getElecPrice().setScale(4, RoundingMode.HALF_EVEN);
                }
            } else if (priceInfoData.getPriceType() == 2) {
                if (t2s.floatValue() >= 0) {
                    if (t2s.compareTo(priceInfoData.getServicePrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t2s = priceInfoData.getServicePrice().setScale(4, RoundingMode.HALF_EVEN);
                }

                if (t2e.floatValue() >= 0) {
                    if (t2e.compareTo(priceInfoData.getElecPrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t2e = priceInfoData.getElecPrice().setScale(4, RoundingMode.HALF_EVEN);
                }
            } else {
                if (t3s.floatValue() >= 0) {
                    if (t3s.compareTo(priceInfoData.getServicePrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t3s = priceInfoData.getServicePrice().setScale(4, RoundingMode.HALF_EVEN);
                }

                if (t3e.floatValue() >= 0) {
                    if (t3e.compareTo(priceInfoData.getElecPrice()) != 0) {
                        throw new BaseException("尖时服务费不一致");
                    }
                } else {
                    t3e = priceInfoData.getElecPrice().setScale(4, RoundingMode.HALF_EVEN);
                }
            }

        }
    }

    /**
     * 校验并批量删除站点价格模版信息
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

    static final String[] startHours = {
        "000000", "003000",
        "010000", "013000",
        "020000", "023000",
        "030000", "033000",
        "040000", "043000",
        "050000", "053000",
        "060000", "063000",
        "070000", "073000",
        "080000", "083000",
        "090000", "093000",
        "100000", "103000",
        "110000", "113000",
        "120000", "123000",
        "130000", "133000",
        "140000", "143000",
        "150000", "153000",
        "160000", "163000",
        "170000", "173000",
        "180000", "183000",
        "190000", "193000",
        "200000", "203000",
        "210000", "213000",
        "220000", "223000",
        "230000", "233000"
    };


}
