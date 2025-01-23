/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.yunkuaichong.annotation;

import java.lang.annotation.*;

/**
 * @author baigod
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YunKuaiChongCmd {

    int value();

}
