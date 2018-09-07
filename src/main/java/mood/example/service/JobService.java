package mood.example.service;


import com.fasterxml.jackson.databind.ser.Serializers;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import mood.annotation.Component;
import mood.annotation.Deploy;
import mood.base.BaseVerticle;

import java.util.List;

@Deploy(Instances = 16,isworker = true)
@Component()
public class JobService extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
    }
}
