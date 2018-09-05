package mood.core;

import mood.annotation.*;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import lombok.Getter;
import lombok.Setter;
import mood.launcher.Launcher;
import mood.launcher.RxJavaLauncher;
import mood.launcher.impl.ClusterLauncher;
import mood.launcher.impl.RxJavaClusterLauncher;
import mood.launcher.impl.RxJavaSigleLauncher;
import mood.launcher.impl.SigleLauncher;
import mood.repository.ClassFactory;
import mood.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
/**
 * <p>Description: mood-vertx-core @CoreServer</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * * @Description: CoreServer 启动类
 * @version: 1.0
 */
public class CoreServer {
    /**
     * 监听端口号
     */
    private int port = 0;

    /**
     * Boss线程数
     */
    private int bossThreads = 1;

    /**
     * Worker线程数
     */
    private int workerThreads = 2;

    /**
     * REST控制器所在包名
     */
    private String controllerBasePackage = "";

    /**
     * 忽略Url列表（不搜索Mapping）
     */
    private static List<String> ignoreUrls = new ArrayList<>(16);

    public CoreServer(int port) {
        this.port=port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public void setControllerBasePackage(String controllerBasePackage) {
        this.controllerBasePackage = controllerBasePackage;
    }

    public static void setIgnoreUrls(List<String> ignoreUrls) {
        CoreServer.ignoreUrls = ignoreUrls;
    }

    /**
     * 程序启动方法
     */
    public void run() throws Exception{
        new ClassFactory().registerCompontent(this.controllerBasePackage);
        Map<String, Verticle> map= Repository.getBeans();
        final boolean isClustered = System.getProperty("isClustered")!=null?System.getProperty("isClustered").equals("true")?true:false:false;
        final Launcher launcher = isClustered ? new ClusterLauncher() : new SigleLauncher();
        final boolean isRxJava=System.getProperty("RxJava")!=null?System.getProperty("RxJava").equals("true")?true:false:false;//判断是RxJava方式还是普通Vertx -DRxJava=true
        final RxJavaLauncher rxJavaLauncher = isClustered ? new RxJavaClusterLauncher() : new RxJavaSigleLauncher();
        if (isRxJava){
            rxJavaLauncher.start((io.vertx.reactivex.core.Vertx vertx) -> {
                //todo Rxjava2 vertx 自动部署
            });
        }else {
            launcher.start(vertx -> {
                for (Map.Entry<String, Verticle> entry:map.entrySet()){
                    for (int i=0;i<entry.getValue().getClass().getAnnotation(Deploy.class).Instances();i++){
                        vertx.deployVerticle(() -> entry.getValue(),new DeploymentOptions().setWorker(entry.getValue().getClass().getAnnotation(Deploy.class).isworker()));
                    }
                }
            });
        }
    }
}
