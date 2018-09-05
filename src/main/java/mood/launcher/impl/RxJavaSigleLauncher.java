package mood.launcher.impl;

import io.vertx.core.VertxOptions;
import io.vertx.reactivex.core.Vertx;
import mood.launcher.RxJavaLauncher;

import java.util.function.Consumer;
/**
 * <p>Description: mood-vertx-Launcher RxJavaSigleLauncher</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 启动Launcher 单机模式 rxjava2
 * @version: 1.0
 */
public class RxJavaSigleLauncher implements RxJavaLauncher {
    @Override
    public void start(Consumer<Vertx> consumer) {
        final VertxOptions options = new VertxOptions();
        final Vertx vertx = Vertx.vertx(options);
        if (null != vertx) consumer.accept(vertx);
    }
}
