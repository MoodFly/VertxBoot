package mood.annotation;


import mood.enums.Autowire;

import java.lang.annotation.*;

/**
 * <p>Description: mood-vertx-annotation @Component</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 扫描Class
 * @version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
    boolean singleton() default false;
    Autowire autowire() default Autowire.NO;
}
