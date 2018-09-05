package mood.example.service;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import mood.annotation.Component;
import mood.annotation.Deploy;

import java.util.List;

@Deploy(Instances = 16,isworker = true)
@Component()
public class JobService extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("當前綫程："+Thread.currentThread().getName());
        Router router=Router.router(this.vertx);
        router.route("/mood")
                .handler(x->{
                    System.out.println(Thread.currentThread().getName() + ",  Request...url(/mood)");
                    x.response().end("Hello Easy-Vertx");
                });
        this.vertx.createHttpServer().requestHandler(router::accept).listen(8088);

    }
}
