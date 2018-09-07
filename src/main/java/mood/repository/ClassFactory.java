package mood.repository;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mood.annotation.Component;
import mood.annotation.Router;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>Description: mood-vertx-repository ClassFactory</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: ClassFactory
 * @version: 1.0
 */
public final class ClassFactory {

    private final static Logger logger = LoggerFactory.getLogger(ClassFactory.class);

    /**
     * 注册Mpping
     * @author: by Mood
     * @param basePackage
     */
    public void registerCompontent(String basePackage) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        Set<Class<?>> BeanClasses = findClassesByPackage(basePackage);
        for (Class<?> controllerClass : BeanClasses) {
            registerClass(controllerClass);
        }
        Repository.CreateBean();
    }

    /**
     * 扫描包路径下所有的class文件
     * @author: by Mood
     * @param packageName
     * @return
     */
    private Set<Class<?>> findClassesByPackage(String packageName) {
        Function<String,Set<Class<?>>> function=x->{
            Set<Class<?>> classes = new LinkedHashSet<>(64);
            String pkgDirName = x.replace('.', '/');
            try {
                Enumeration<URL> urls = ClassFactory.class.getClassLoader().getResources(pkgDirName);
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                        findClassesByFile(filePath, x, classes);
                    } else if ("jar".equals(protocol)) {
                        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        findClassesByJar(x, jar, classes);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return classes;
        };
        if (packageName.endsWith("*")){
            //todo 待优化
            packageName=packageName.substring(0,packageName.length()-2);
            Set<Class<?>> classes = new LinkedHashSet<>(128);
                ClassLoader loader =  Thread.currentThread().getContextClassLoader();
                String pkgDirName = packageName.replace('.', '/');
                URL url = loader.getResource(pkgDirName);
                Set<String> packages=getPackageNameFromDir(url.getPath(),packageName,true);
                packages.forEach(x->{
                    String packagename = x.replace('/', '.');
                    Set<Class<?>> childclasses=function.apply(packagename);
                    classes.addAll(childclasses);
                });
            return classes;
        }else{
            return function.apply(packageName);
        }
    }
    /**
     * 扫描包路径下所有的class文件
     * @author: by Mood
     * @param basepackage
     * @return
     */
    private static Set<String> getPackageNameFromDir(String filePath,String basepackage,boolean isRecursion) {
        Set<String> packageNameScan = new HashSet<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                if (isRecursion) {
                    packageNameScan.add(basepackage+"."+childFile.getName());
                }
            }
        }
        return packageNameScan;
    }

    /**
     * 扫描包下的所有class文件
     * @author: by Mood
     * @param path
     * @param packageName
     * @param classes
     */
    private void findClassesByFile(String path, String packageName, Set<Class<?>> classes) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(filter -> filter.isDirectory() || filter.getName().endsWith("class"));
        for (File f : files) {
            if (f.isDirectory()) {
                findClassesByFile(packageName + "." + f.getName(), path + "/" + f.getName(), classes);
                continue;
            }
            // 获取类名，去掉 ".class" 后缀
            String className = f.getName();
            className = packageName + "." + className.substring(0, className.length() - 6);
            // 加载类
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.error("Class not found, {}", className);
            }
            if (clazz != null && clazz.getAnnotation(Component.class) != null) {
                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }
    }
    /**
     * 扫描包路径下的所有class文件
     *
     * @param packageName 包名
     * @param jar jar文件
     * @param classes 保存包路径下class的集合
     */
    private static void findClassesByJar(String packageName, JarFile jar, Set<Class<?>> classes) {
        String pkgDir = packageName.replace(".", "/");
        Enumeration<JarEntry> entry = jar.entries();
        while (entry.hasMoreElements()) {
            JarEntry jarEntry = entry.nextElement();

            String jarName = jarEntry.getName();
            if (jarName.charAt(0) == '/') {
                jarName = jarName.substring(1);
            }
            if (jarEntry.isDirectory() || !jarName.startsWith(pkgDir) || !jarName.endsWith(".class")) {
                continue;
            }
            String[] classNameSplit = jarName.split("/");
            String className = packageName + "." + classNameSplit[classNameSplit.length - 1];
            if(className.endsWith(".class")) {
                className = className.substring(0, className.length() - 6);
            }
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.error("Class not found, {}", className);
            }
            if (clazz != null && clazz.getAnnotation(Component.class) != null) {
                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }
    }

    /**
     * 注册类
     * 
     * @param clazz
     */
    private void registerClass(Class<?> clazz) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        String className = clazz.getName();
        ComponentBean bean = new ComponentBean(clazz, clazz.getAnnotation(Component.class).singleton());
        Repository.registerBean(className, bean);
        String url = null;
        Router requestMapping = clazz.getAnnotation(Router.class);
        if (requestMapping != null) {
            url = requestMapping.value();
        }
        if (requestMapping!=null&&requestMapping.Method().equals("GET")){
            ControllerInfoMapping controllerInfoMapping=new ControllerInfoMapping();
            controllerInfoMapping.setUrl(url);
            controllerInfoMapping.setClassName(className);
            Repository.registerGetMapping(url,controllerInfoMapping);
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            RouterMappingRegisterStrategy strategy = null;
            // 遍历所有method，生成ControllerMapping并注册。
            if(method.getAnnotation(Router.class) != null&& method.getAnnotation(Router.class).Method().equals("GET")) {
                strategy = new GetMappingRegisterStrategy();
            }
            if(strategy != null) {
                RouterMappingRegisterContext mappingRegCtx = new RouterMappingRegisterContext(strategy);
                mappingRegCtx.registerMapping(clazz, url, method);
            }
        }
    }
}
