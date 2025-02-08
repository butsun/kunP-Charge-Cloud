package org.dromara.kp.business.api;

import org.dromara.kp.protocol.yunkuaichong.domain.dto.*;
import org.dromara.kp.protocol.yunkuaichong.domain.model.PricingModel;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:08
 **/
public interface PileChargeService {

    PileTryChargeResponse tryCharge(PileTryChargeRequest pileTryChargeRequest);

    TransactionRecordAck pileChargeTransactionRecord(TransactionRecord transactionRecord);

    void refreshChargeOrder(ChargingProgressProto chargingProgressProto);

    PricingModel getPilePricingModel(String pileCode);
}
