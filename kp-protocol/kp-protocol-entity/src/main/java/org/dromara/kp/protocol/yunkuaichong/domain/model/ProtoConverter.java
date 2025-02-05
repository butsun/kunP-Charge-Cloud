/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.yunkuaichong.domain.model;


import org.dromara.kp.protocol.yunkuaichong.domain.dto.FlagPriceProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PeriodProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PricingModelProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.TracerProto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baigod
 */
public class ProtoConverter {

    public static PricingModelProto toPricingModel(PricingModel pricingModel) {
        // 创建 PricingModelProto 实例
        PricingModelProto.PricingModelProtoBuilder builder = PricingModelProto.builder();

        // 设置字段
        builder.type(PricingModelProto.PricingModelType.valueOf(pricingModel.getType().name()));
        builder.rule(PricingModelProto.PricingModelRule.valueOf(pricingModel.getRule().name()));
        builder.standardElec(pricingModel.getStandardElec().toPlainString());
        builder.standardServ(pricingModel.getStandardServ().toPlainString());

        // 转换 flagPriceList
        Map<Integer, FlagPriceProto> flagPrices = new HashMap<>();
        for (Map.Entry<PeriodProto.PricingModelFlag, PricingModel.FlagPrice> entry : pricingModel.getFlagPriceList().entrySet()) {
            PeriodProto.PricingModelFlag flagKey = entry.getKey();
            PricingModel.FlagPrice flagPrice = entry.getValue();
            FlagPriceProto flagPriceProto = FlagPriceProto.builder()
                .flag(PeriodProto.PricingModelFlag.valueOf(flagKey.name())) // 枚举转换
                .elec(flagPrice.getElec().toPlainString())
                .serv(flagPrice.getServ().toPlainString())
                .build();
            flagPrices.put(flagKey.ordinal(), flagPriceProto);
        }

        // 转换 PeriodsList
        List<PeriodProto> periodProto = pricingModel.getPeriodsList().stream()
            .map(period -> PeriodProto.builder()
                .sn(period.getSn())
                .begin(period.getBegin().toString()) // 假设 begin 是 LocalTime, 转换为字符串
                .end(period.getEnd().toString())// 假设 end 是 LocalTime, 转换为字符串
                .flag(PeriodProto.PricingModelFlag.valueOf(period.getFlag().name()))
                .build()
            ).toList();
        builder.flagPrices(flagPrices); // 按 ordinal 值作为 key 存入
        builder.periods(periodProto);
        return builder.build();
    }
}
