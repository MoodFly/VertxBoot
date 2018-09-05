package mood.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: mood-vertx-annotation @Value</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * * @Description: 自动注入Confisg
 * @version: 1.0
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    boolean required() default true;
}
