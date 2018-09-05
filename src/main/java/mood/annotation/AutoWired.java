package mood.annotation;

import java.lang.annotation.*;
/**
 * <p>Description: mood-vertx-annotation @AutoWired</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * * @Description: 自动注入Class
 * @version: 1.0
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoWired {
    boolean required() default true;
}
