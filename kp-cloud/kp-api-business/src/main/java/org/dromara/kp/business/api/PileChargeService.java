package org.dromara.kp.business.api;

import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileTryChargeRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileTryChargeResponse;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:08
 **/
public interface PileChargeService {

    PileTryChargeResponse tryCharge(PileTryChargeRequest pileTryChargeRequest);
}
