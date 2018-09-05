package mood.repository;

import java.lang.reflect.Method;

/**
 * <p>Description: mood-vertx-repository MappingRegisterStrategy</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @version: 1.0
 */
public interface MappingRegisterStrategy {
    
    /**
     * 注册映射
     * @param clazz
     * @param baseUrl
     * @param method
     */
    void register(Class<?> clazz, String baseUrl, Method method);

}
