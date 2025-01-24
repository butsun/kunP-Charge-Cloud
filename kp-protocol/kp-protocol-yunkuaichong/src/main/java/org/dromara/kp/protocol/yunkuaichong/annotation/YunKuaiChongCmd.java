
package org.dromara.kp.protocol.yunkuaichong.annotation;

import org.dromara.kp.protocol.yunkuaichong.domain.enums.Test;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongDownlinkCmdEnum;
import org.dromara.kp.protocol.yunkuaichong.domain.enums.YunKuaiChongUplinkCmdEnum;

import java.lang.annotation.*;

/**
 * @author but
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YunKuaiChongCmd {

    int value() default 0;

    YunKuaiChongUplinkCmdEnum upCmd() default YunKuaiChongUplinkCmdEnum.UNKNOWN;

    YunKuaiChongDownlinkCmdEnum downCmd() default YunKuaiChongDownlinkCmdEnum.UNKNOWN;

}
