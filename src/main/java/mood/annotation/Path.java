package mood.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: mood-vertx-annotation @Path</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 路径变量注解
 * @version: 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {
    
    String value() default "";

}
