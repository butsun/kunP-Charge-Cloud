package org.dromara.kp.system.dubbo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.kp.business.api.PileLeftCycleService;
import org.dromara.kp.business.api.domain.ConnectStatusConvertEnum;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.GunRunStatusProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.HeartBeatRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.LoginResponse;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileLostEvent;
import org.dromara.kp.system.domain.KpConnector;
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.service.IKpConnectorService;
import org.dromara.kp.system.service.IKpEquipmentService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:07
 **/
@DubboService
@Slf4j
@RequiredArgsConstructor
public class PileLeftCycleClient implements PileLeftCycleService {

    private final IKpEquipmentService kpEquipmentService;

    private final IKpConnectorService kpConnectorService;
    @Override
    public LoginResponse authPileLogin(String pileCode, int netType) {
        KpEquipment equipment =  kpEquipmentService.queryByEquipmentNo(pileCode);
        boolean flag = false;
        if (Objects.nonNull(equipment)) {
            //设备存在 开始上电
            equipment.setNetType(netType);
            equipment.setIsWorking(0);
            equipment.setOnlineTm(DateUtil.date());
            equipment.setSyncTm(DateUtil.date());
            kpEquipmentService.update(equipment);
            flag = true;
        }
        return LoginResponse.builder()
            .pileCode(pileCode)
            .success(flag)
            .build();
    }

    @Override
    public void syncTime(String pileCode, DateTime date) {
        KpEquipment equipment =  kpEquipmentService.queryByEquipmentNo(pileCode);
        equipment.setSyncTm(DateUtil.date());
        kpEquipmentService.update(equipment);
    }


    @Override
    public void refreshPileStatus(HeartBeatRequest heartBeatRequest) {
        KpEquipment equipment =  kpEquipmentService.queryByEquipmentNo(heartBeatRequest.getPileCode());
        equipment.setIsWorking(0);
        kpEquipmentService.update(equipment);
    }


    @Override
    public void refreshGunStatus(GunRunStatusProto gunRunStatusProto) {
        Integer gunState = ConnectStatusConvertEnum.getCode(gunRunStatusProto.getGunRunStatus().name());
        kpConnectorService.updateStatus(gunRunStatusProto.getPileCode(), gunRunStatusProto.getGunCode(),gunState);
    }


    @Override
    public void lost(PileLostEvent pileLostEvent) {
        Set<String> pileCodes = pileLostEvent.getPileCode();
        for (String pileCode : pileCodes) {
            kpEquipmentService.pileLost(pileCode);
        }
    }

}
