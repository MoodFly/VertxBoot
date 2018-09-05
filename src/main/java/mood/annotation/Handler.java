package mood.annotation;

import java.lang.annotation.*;
/**
 * <p>Description: mood-vertx-annotation @Handler</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 自动设置Handler
 * @version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Handler {
    String value() default "";
}
