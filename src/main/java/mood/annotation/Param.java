package mood.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: mood-vertx-annotation @Param</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 参数注解
 * @version: 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String value() default "";

    boolean required() default true;
}
