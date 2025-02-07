package org.dromara.kp.system.dubbo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.kp.business.api.PileLeftCycleService;
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.domain.vo.KpEquipmentVo;
import org.dromara.kp.system.service.IKpConnectorService;
import org.dromara.kp.system.service.IKpEquipmentService;

import java.util.Objects;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:07
 **/
@DubboService
@Slf4j
@RequiredArgsConstructor
public class PileLeftCycleClient  implements PileLeftCycleService {

    private final IKpEquipmentService kpEquipmentService;

    private final IKpConnectorService kpConnectorService;
    @Override
    public boolean authPileLogin(String pileCode,int netType) {
        KpEquipment equipment =  kpEquipmentService.queryByEquipmentNo(pileCode);
        if (Objects.isNull(equipment)) {
            return false;
        }
        //设备存在 开始上电
        equipment.setNetType(netType);
        equipment.setSyncTm(DateUtil.date());
        kpEquipmentService.update(equipment);
        return true;
    }

    @Override
    public void syncTime(String pileCode, DateTime date) {
        KpEquipment equipment =  kpEquipmentService.queryByEquipmentNo(pileCode);
        equipment.setSyncTm(DateUtil.date());
        kpEquipmentService.update(equipment);
    }
}
