package mood.repository;


import mood.annotation.Router;

import java.lang.reflect.Method;
/**
 * <p>Description: mood-vertx-repository RouterMappingRegisterStrategy</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 请求映射策略
 * @version: 1.0
 */
public class RouterMappingRegisterStrategy extends AbstractMappingRegisterStrategy {
    /**
     * 得到控制器方法的Url
     *
     * @param method
     * @return
     */
    @Override
    String getMethodUrl(Method method) {
        if(method.getAnnotation(Router.class) != null) {
            return method.getAnnotation(Router.class).value();
        }
        return "";
    }

    /**
     * 得到Http请求的方法类型
     *
     * @return
     */
    @Override
    String getHttpMethod(Method method) {
        if(method.getAnnotation(Router.class) != null) {
            return method.getAnnotation(Router.class).Method();
        }
        return "GET";
    }

    /**
     * 注册Mapping
     *
     * @param url
     * @param mapping
     */
    @Override
    void registerMapping(String url, ControllerInfoMapping mapping) {

    }
}
