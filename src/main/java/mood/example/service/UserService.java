package mood.example.service;

import mood.annotation.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import mood.base.BaseVerticle;
import mood.example.dao.UserDao;
import mood.example.entity.User;
import mood.example.router.UserRouter;

import java.util.List;

@Deploy(Instances = 4)
@Component()
public class UserService  extends BaseVerticle{
    @Override
    public void start() throws Exception {
        super.start();
    }
}
