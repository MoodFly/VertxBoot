package mood.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: mood-vertx-annotation @Deploy</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @version: 1.0
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Router {
    String value() default "";
    String Method() default "GET";
}
