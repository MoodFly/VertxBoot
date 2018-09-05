package mood.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: mood-vertx-annotation @Deploy</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 自动部署Verticle
 * @version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Deploy {
    boolean singleton() default false;
    boolean isworker() default false;
    int Instances() default 1;
}
