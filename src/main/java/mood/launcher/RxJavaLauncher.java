package mood.launcher;


import io.vertx.reactivex.core.Vertx;

import java.util.function.Consumer;
/**
 * <p>Description: mood-vertx-Launcher RxJavaLauncher</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 启动Launcher Rxjava2
 * @version: 1.0
 */
public interface RxJavaLauncher {
    void start(Consumer<Vertx> startConsumer);
}
