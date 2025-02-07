package org.dromara.kp.business.api;

import cn.hutool.core.date.DateTime;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:08
 **/
public interface PileLeftCycleService {
    boolean authPileLogin(String pileCode,int netType);

    void syncTime(String pileCode, DateTime date);
}
