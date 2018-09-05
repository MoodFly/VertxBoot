package mood.repository;

import java.lang.reflect.Method;

/*
 * <p>Description: mood-vertx-repository RouterMappingRegisterContext</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 请求映射策略上下文类
 * @version: 1.0
 */
public final class RouterMappingRegisterContext {
    
    private RouterMappingRegisterStrategy strategy;

    public RouterMappingRegisterContext(RouterMappingRegisterStrategy strategy) {
        this.strategy = strategy;  
    }
    /**
     * 注册 Mapping
     * @param clazz
     * @param baseUrl
     * @param method
     */
    public void registerMapping(Class<?> clazz, String baseUrl, Method method) {
        if(this.strategy == null) {
            return;
        }
        this.strategy.register(clazz, baseUrl, method);
    }
    
}
