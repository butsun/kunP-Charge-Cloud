package org.dromara.kp.system.job;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.client.model.ExecuteResult;
import com.aizuda.snailjob.common.core.util.JsonUtil;
import com.aizuda.snailjob.common.log.SnailJobLog;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.kp.system.domain.KpChargeOrder;
import org.dromara.kp.system.mapper.KpChargeOrderMapper;
import org.springframework.stereotype.Component;

import java.sql.Wrapper;

/**
 * @program: RuoYi-Cloud-Plus
 * @description:
 * @author: sunjun
 * @create: 13:21
 **/
@Component
@RequiredArgsConstructor
@JobExecutor(name = "InvalidChargeOrderJobExecutor")
public class InvalidChargeOrderJobExecutor {

    private final KpChargeOrderMapper kpChargeOrderMapper;

    public ExecuteResult jobExecute(JobArgs jobArgs) {
        //找到订单开始时间在30分钟前  但是至今未更新数据
        int update = kpChargeOrderMapper.update(Wrappers.lambdaUpdate(KpChargeOrder.class)
            .eq(KpChargeOrder::getDelFlag, 0)
            .lt(KpChargeOrder::getStartTime, DateUtil.date().offset(DateField.MINUTE, -30))
            .eq(KpChargeOrder::getTotalPower, 0)
            .eq(KpChargeOrder::getStartChargeSeqStat, 1)
            .set(KpChargeOrder::getDelFlag, 1)
        );
        return ExecuteResult.success("扫描成功，处理 " + update + " 条启动中订单");
    }


}
