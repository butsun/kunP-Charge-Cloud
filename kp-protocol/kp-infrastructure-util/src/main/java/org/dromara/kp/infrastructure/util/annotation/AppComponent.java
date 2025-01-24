
package org.dromara.kp.infrastructure.util.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author but
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ConditionalOnExpression("'${service.type:null}'=='monolith' || '${service.type:null}'=='app'")
@Component
public @interface AppComponent {


}
