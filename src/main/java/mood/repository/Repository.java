package mood.repository;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mood.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * <p>Description: mood-vertx-repository Repository</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 定义的基类用于存储被注解表示类注册到内存中
 * @version: 1.0
 */
public class Repository {
    private static final Logger logger = LoggerFactory.getLogger(Repository.class);
    private static final Map<String, ControllerInfoMapping> getMappings = new HashMap<>(64);
    private static final Map<String, ControllerInfoMapping> postMappings = new HashMap<>(64);
    private static final Map<String, ControllerInfoMapping> putMappings = new HashMap<>(64);
    private static final Map<String, ControllerInfoMapping> deleteMappings = new HashMap<>(64);
    private static final Map<String, ControllerInfoMapping> patchMappings = new HashMap<>(64);
    /**
     * 缓存掃描类
     */
    private static final Map<String, ComponentBean> vertxComponentbeans = new HashMap<>(128);
    /**
     * 缓存Verticle类
     */
    private static final Map<String, Verticle> vertxDepolybeans = new ConcurrentHashMap<>(128);
    /**
     * 缓存类
     */
    private static final Map<String, Object> vertxbeans = new ConcurrentHashMap<>(128);
    /**
     * 缓存單例类
     */
    private static final Map<String, Object> vertxSingletonbeans = new ConcurrentHashMap<>(128);
    /**
     * 缓存处理循环依赖
     */
    private static final Map<String, Object> vertxVerticlebeansL1 = new ConcurrentHashMap<>(128);
    /**
     * 缓存处理循环依赖
     */
    private static final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));
    static{
        vertxbeans.put("io.vertx.core.Vertx", Vertx.vertx());
        vertxbeans.put("io.vertx.reactivex.core.Vertx", io.vertx.reactivex.core.Vertx.vertx());
         vertxbeans.put("io.vertx.rxjava.core.Vertx", io.vertx.rxjava.core.Vertx.vertx());
    }
    /**
     * 注册 Bean
     *
     * @param name
     * @param clazz
     */
    public static void registerBean(String name, ComponentBean clazz) {
        //先把所有Component掃描到，放入緩存
        vertxComponentbeans.put(name, clazz);
    }
    /**
     * 創建 Bean
     * @author: by Mood
     */
    public static void CreateBean() throws IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        for(Map.Entry<String,ComponentBean> entry:vertxComponentbeans.entrySet()){
            injection(vertxbeans,entry.getValue().getClazz());
            //掃描到有@Deploy注解，放入vertxDepolybeans緩存
            if (entry.getValue().getClazz().getAnnotation(Deploy.class)!=null){
                vertxDepolybeans.put(entry.getKey(),(Verticle)vertxbeans.get(entry.getKey()));
            }
        }
        //处理循环引用
        for (Map.Entry<String, Object> bean:vertxbeans.entrySet()){
            reinjection(bean.getValue().getClass());
        }
        for (Map.Entry<String, Object> bean:vertxbeans.entrySet()){
            initMethod(bean.getValue().getClass());
        }

    }
    /**
     * @param vertxbeans
     * @param classes
     * @Description ：自動注入 Bean
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void injection(Map<String,Object> vertxbeans, Class classes) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Field[] fields = classes.getDeclaredFields();
        boolean fieldExist;
        Object beanObj;
        if (null == vertxbeans.get(classes.getName())) {
             beanObj = classes.newInstance();
        }else {
             beanObj=vertxbeans.get(classes.getName());
        }
        synchronized (vertxSingletonbeans) {
            if (vertxComponentbeans.get(classes.getName()) != null &&vertxComponentbeans.get(classes.getName()).isSingleton()){
                if (!vertxSingletonbeans.containsKey(classes.getName())) {
                    vertxSingletonbeans.put(classes.getName(), beanObj);
                }
            }
        }
        Class fieldClass=null;
        for (Field field : fields) {
                fieldExist = field.isAnnotationPresent(AutoWired.class);
                if (fieldExist) {
                    String classtype = field.getGenericType().toString();
                    if (classtype.startsWith("class")){
                        fieldClass = Class.forName(classtype.substring(6));
                    }else if(classtype.startsWith("interface")){
                        fieldClass = Class.forName(classtype.substring(10));
                    }
                    field.setAccessible(true);
                    if (fieldClass.isAnnotationPresent(Component.class)) {
                        if (vertxComponentbeans.get(fieldClass.getName()) != null
                                &&(vertxComponentbeans.get(classes.getName()).isSingleton()
                                &&vertxComponentbeans.get(fieldClass.getName()).isSingleton()) ) {
                            field.set(beanObj,getSingleton(fieldClass.getName(),true));
                        } else {
                            injection(vertxbeans,fieldClass);
                            field.set(beanObj, vertxbeans.get(fieldClass.getName())!=null?vertxbeans.get(fieldClass.getName()):vertxComponentbeans.get(fieldClass.getName()).getClazz().newInstance());
                        }
                    } else {
                        field.set(beanObj, vertxbeans.get(fieldClass.getName())!=null?vertxbeans.get(fieldClass.getName()):fieldClass.newInstance());
                    }
                }else {
                    boolean fieldValueExist = field.isAnnotationPresent(Value.class);
                    if (fieldValueExist){
                        String classtype = field.getGenericType().toString();
                        if (classtype.startsWith("class")){
                            fieldClass = Class.forName(classtype.substring(6));
                        }else if(classtype.startsWith("interface")){
                            fieldClass = Class.forName(classtype.substring(10));
                        }
                        field.setAccessible(true);
                        field.set(beanObj,fieldClass.newInstance());
                    }
                }
            }
        if (vertxComponentbeans.get(classes.getName()).isSingleton()){
            synchronized (vertxSingletonbeans) {
                        vertxSingletonbeans.putIfAbsent(classes.getName(), beanObj);
            }
        }
        vertxbeans.put(classes.getName(), beanObj);
    }

    /**
     *
     * @Description ：處理循環依賴
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void reinjection(Class classes)  throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Field[] fields=classes.getDeclaredFields();
        boolean fieldExist;
        Object beanObj=vertxbeans.get(classes.getName());
        for(Field field:fields) {
            fieldExist=field.isAnnotationPresent(AutoWired.class);
            if(fieldExist) {
                String classtype=field.getGenericType().toString();
                Class fieldClass=null;
                if (classtype.startsWith("class")){
                    fieldClass = Class.forName(classtype.substring(6));
                }else if(classtype.startsWith("interface")){
                    fieldClass = Class.forName(classtype.substring(10));
                }
                field.setAccessible(true);
                if (fieldClass.isAnnotationPresent(Component.class)) {
                    if (vertxComponentbeans.get(fieldClass.getName()) != null
                            &&(vertxComponentbeans.get(classes.getName()).isSingleton()
                            &&vertxComponentbeans.get(fieldClass.getName()).isSingleton()) ) {
                            field.set(beanObj,getSingleton(fieldClass.getName(),true));
                    }
                }
            }
        }
    }
    /**
     * 得到所有的deploy Bean
     * @return
     */
    public static Map<String, Verticle> getBeans() {
        return vertxDepolybeans;
    }
    /**
     * 得到Controller Bean
     * @author: by Mood
     * @param name
     * @return
     */
    public static Object getBean(String name) throws IllegalAccessException, InstantiationException {
        return vertxComponentbeans.get(name).getClazz().newInstance();
    }

    /**
     * 获取单例
     * @author: by Mood
     * @return
     */
    protected static Object getSingleton(String beanName, boolean allowEarlyReference) {
        Object singletonObject = vertxSingletonbeans.get(beanName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (vertxSingletonbeans) {
                singletonObject = vertxSingletonbeans.get(beanName);
                if (singletonObject == null && allowEarlyReference) {
                    Object singleton = vertxVerticlebeansL1.get(beanName);
                    if (singleton != null) {
                        vertxSingletonbeans.put(beanName, singleton);
                        vertxVerticlebeansL1.remove(beanName);
                    }
                }
            }
        }
        return (singletonObject != null ? singletonObject : null);
    }
    public static boolean isSingletonCurrentlyInCreation(String beanName) {
        return singletonsCurrentlyInCreation.contains(beanName);
    }
    /**
     * @author: by Mood
     * @param clazz
     */
    private static void initMethod(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if(method.getAnnotation(Init.class) != null) {
                method.setAccessible(true);
                method.invoke(vertxbeans.get(clazz.getName()));
            }
        }
    }
    /**
     * 注册 Get Mapping
     *
     * @param url
     * @param mapping
     */
    public static void registerGetMapping(String url, ControllerInfoMapping mapping) {
        getMappings.put(url, mapping);
    }
    /**
     * 得到Get映射哈希表
     *
     * @return
     */
    public static Map<String, ControllerInfoMapping> getGetMappings() {
        return getMappings;
    }
    /**
     * 注册 Post Mapping
     *
     * @param url
     * @param mapping
     */
    public static void registerPostMapping(String url, ControllerInfoMapping mapping) {
        postMappings.put(url, mapping);
    }
    /**
     * 得到Post映射哈希表
     *
     * @return
     */
    public static Map<String, ControllerInfoMapping> getPostMappings() {
        return postMappings;
    }
    /**
     * 注册 Put Mapping
     *
     * @param url
     * @param mapping
     */
    public static void registerPutMapping(String url, ControllerInfoMapping mapping) {
        putMappings.put(url, mapping);
    }
    /**
     * 得到Put映射哈希表
     *
     * @return
     */
    public static Map<String, ControllerInfoMapping> getPutMappings() {
        return putMappings;
    }
    /**
     * 注册 Delete Mapping
     *
     * @param url
     * @param mapping
     */
    public static void registerDeleteMapping(String url, ControllerInfoMapping mapping) {
        deleteMappings.put(url, mapping);
    }
    /**
     * 得到Delete映射哈希表
     *
     * @return
     */
    public static Map<String, ControllerInfoMapping> getDeleteMappings() {
        return deleteMappings;
    }
    /**
     * 注册 Patch Mapping
     *
     * @param url
     * @param mapping
     */
    public static void registerPatchMapping(String url, ControllerInfoMapping mapping) {
        patchMappings.put(url, mapping);
    }
    /**
     * 得到Patch映射哈希表
     *
     * @return
     */
    public static Map<String, ControllerInfoMapping> getPatchMappings() {
        return patchMappings;
    }
}
