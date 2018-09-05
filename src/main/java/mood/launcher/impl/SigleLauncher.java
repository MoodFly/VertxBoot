package mood.launcher.impl;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import mood.launcher.Launcher;

import java.util.function.Consumer;
/**
 * <p>Description: mood-vertx-Launcher SigleLauncher</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 启动Launcher 单机模式
 * @version: 1.0
 */
public class SigleLauncher implements Launcher {
    @Override
    public void start(Consumer<Vertx> consumer) {
        final VertxOptions options = new VertxOptions();
        final Vertx vertx = Vertx.vertx(options);
        if (null != vertx) consumer.accept(vertx);
    }
}
