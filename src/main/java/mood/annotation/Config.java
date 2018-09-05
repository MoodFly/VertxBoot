package mood.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: mood-vertx-annotation @Init</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 初始化注解Config
 * @version: 1.0
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Config {
}
