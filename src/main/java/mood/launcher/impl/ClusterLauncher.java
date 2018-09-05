package mood.launcher.impl;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import mood.launcher.Launcher;

import java.util.function.Consumer;
/**
 * <p>Description: mood-vertx-Launcher ClusterLauncher</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 启动Launcher 集群模式
 * @version: 1.0
 */
public class ClusterLauncher implements Launcher {
    @Override
        public void start(final Consumer<Vertx> consumer) {
            final VertxOptions options = new VertxOptions();
            Vertx.clusteredVertx(options, handler -> {
                if (handler.succeeded()) {
                    final Vertx vertx = handler.result();
                    if (null != vertx) {
                        consumer.accept(vertx);
                    }
                } else {
                    final Throwable ex = handler.cause();
                    if (null != ex) {
                        ex.printStackTrace();
                    }
                }
            });
    }
}
