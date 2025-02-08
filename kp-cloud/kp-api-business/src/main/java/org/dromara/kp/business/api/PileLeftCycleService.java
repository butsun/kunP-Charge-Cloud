package org.dromara.kp.business.api;

import cn.hutool.core.date.DateTime;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.GunRunStatusProto;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.HeartBeatRequest;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.LoginResponse;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileLostEvent;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:08
 **/
public interface PileLeftCycleService {
    LoginResponse authPileLogin(String pileCode, int netType);

    void syncTime(String pileCode, DateTime date);

    /**
     * 刷新枪状态
     * @param heartBeatRequest
     */
    void refreshPileStatus(HeartBeatRequest heartBeatRequest);

    void refreshGunStatus(GunRunStatusProto gunRunStatusProto);

    void lost(PileLostEvent pileLostEvent);
}
