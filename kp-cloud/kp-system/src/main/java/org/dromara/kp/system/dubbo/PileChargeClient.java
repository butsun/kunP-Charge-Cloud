package org.dromara.kp.system.dubbo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.kp.business.api.PileChargeService;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileTryChargeRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileTryChargeResponse;
import org.dromara.kp.system.domain.KpChargeOrder;
import org.dromara.kp.system.domain.KpConnector;
import org.dromara.kp.system.domain.KpEquipment;
import org.dromara.kp.system.service.*;

import java.util.Date;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:07
 **/
@DubboService
@Slf4j
@RequiredArgsConstructor
public class PileChargeClient implements PileChargeService {

    private final IKpEquipmentService equipmentService;
    private final IKpConnectorService connectorService;
    private final IKpStationService stationService;
    private final IKpOperatorService operatorService;
    private final IKpChargeOrderService chargeOrderService;


    @Override
    public PileTryChargeResponse tryCharge(PileTryChargeRequest pileTryChargeRequest) {
        String pileCode = pileTryChargeRequest.getPileCode();
        String gunNo = pileTryChargeRequest.getGunNo();
        long cardNo = pileTryChargeRequest.getCardNo();
        int chargeType = pileTryChargeRequest.getChargeType();
        String carVin = pileTryChargeRequest.getCarVin();
        Date startDate = DateUtil.date();

        KpEquipment equipment = equipmentService.queryByEquipmentNo(pileCode);
        KpConnector connector = connectorService.queryByNo(pileCode, Integer.parseInt(gunNo));

        //todo  还有卡校验等操作
        verifyChargeSession();
        String tradeNo = generateChargeTradeNo(pileCode, gunNo, startDate);
        String startChargeSeq = "OKP" + tradeNo;
        KpChargeOrder kpChargeOrder = new KpChargeOrder();
        kpChargeOrder.setStartChargeSeq(startChargeSeq);
        kpChargeOrder.setTradeNo(tradeNo);
        kpChargeOrder.setStartChargeSeqStat(1);
        kpChargeOrder.setStationId(equipment.getStationId());
        kpChargeOrder.setConnectorId(connector.getId());
        kpChargeOrder.setEquipmentId(equipment.getId());
        kpChargeOrder.setOperatorId(equipment.getOperatorId());
        kpChargeOrder.setStartTime(startDate);
        kpChargeOrder.setCarVin(carVin);
        kpChargeOrder.setConnectorNo(connector.getConnectorNo());
        kpChargeOrder.setEquipmentNo(equipment.getEquipmentNo());
        return PileTryChargeResponse.builder()
            .tradeNo(tradeNo)
            .gunNo(gunNo)
            .cardNo(pileTryChargeRequest.getCardNo())
            .failReason(0)
            .pileCode(pileCode)
            .success(true)
            .build();
    }

    private String generateChargeTradeNo(String pileCode, String gunNo, Date startDate) {
        return pileCode + gunNo + DateUtil.format(startDate, "yyyyMMddHHmmss") + "01";
    }

    private boolean verifyChargeSession() {
        return true;
    }
}
