package mood.base;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import mood.core.CoreServer;


public class BaseVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        Router router=Router.router(this.vertx);
        router.route().handler(
           x-> {DispatchRouter.dispath(x);}
        );
        this.vertx.createHttpServer().requestHandler(router::accept).listen(CoreServer.getPort());
    }
}
