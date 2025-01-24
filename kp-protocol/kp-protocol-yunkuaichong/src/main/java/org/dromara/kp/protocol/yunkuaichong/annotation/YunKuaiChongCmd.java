
package org.dromara.kp.protocol.yunkuaichong.annotation;

import java.lang.annotation.*;

/**
 * @author but
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YunKuaiChongCmd {

    int value();

}
