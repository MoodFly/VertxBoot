package mood.launcher;

import io.vertx.core.Vertx;

import java.util.function.Consumer;
/**
 * <p>Description: mood-vertx-Launcher Launcher</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @Description: 启动Launcher
 * @version: 1.0
 */
public interface Launcher {
    void start(Consumer<Vertx> startConsumer);
}
