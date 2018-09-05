package mood.example.service;

import mood.annotation.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import mood.example.dao.UserDao;
import mood.example.entity.User;
import mood.example.router.UserRouter;

import java.util.List;

@Deploy(Instances = 4)
@Component(singleton = true)
public class UserService extends AbstractVerticle {

    @AutoWired
    private UserDao userDao;
    @AutoWired
    UserRouter userRouter;
    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("當前綫程："+Thread.currentThread().getName());
        Router router=Router.router(this.vertx);
        router.route("/mood")
                .handler(x->{
                    System.out.println(Thread.currentThread().getName() + ",  Request...url(/mood)");
                    x.response().end(queryUserAll().toString());
                });
        this.vertx.createHttpServer().requestHandler(router::accept).listen(8089);
    }
    public List<User> queryUserAll(){
        return userDao.queryUserAll();
    }
    public User queryUserById(int id){
        return userDao.queryUserById(id);
    }
    public int saveUser(User user){
        return userDao.saveUser(user);
    }
    public int updayeUser(User user){
        return userDao.updayeUser(user);
    }
    public int deleteUser(User user){
        return userDao.deleteUser(user);
    }
}
