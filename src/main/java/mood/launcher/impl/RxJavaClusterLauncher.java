package mood.launcher.impl;

import io.vertx.core.VertxOptions;
import io.vertx.reactivex.core.Vertx;
import mood.launcher.RxJavaLauncher;

import java.util.function.Consumer;
/**
 * <p>Description: mood-vertx-Launcher RxJavaClusterLauncher</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 启动Launcher 集群模式 rxjava2
 * @version: 1.0
 */
public class RxJavaClusterLauncher implements RxJavaLauncher {
    @Override
        public void start(final Consumer<Vertx> consumer) {
            final VertxOptions options = new VertxOptions();
            Vertx.rxClusteredVertx(options)
                    .doOnSuccess(consumer::accept)
                    .doOnError(error -> {
                        if (null != error) {
                            error.printStackTrace();
                        }
                    }).subscribe();
    }
}
