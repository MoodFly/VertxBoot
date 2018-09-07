package mood.example.router;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import io.vertx.ext.web.RoutingContext;
import mood.annotation.*;
import mood.example.dao.UserDao;
import mood.example.entity.User;
import mood.example.service.UserService;

@Component()
@Router("/user")
public class UserRouter {
    @AutoWired
    private UserDao userDao;
    @Router(value = "/save",Method = "POST")
    public void saveUserInfo(@Param String name,@Param String job,@Param String sex){

    }
    @Router(value = "/update",Method = "POST")
    public void updateUserInfo(@Param Integer id,@Param String name,@Param String job,@Param String sex){
    }
    @Router("/getUser")
    public void queryUserInfo(RoutingContext routingContext,String id){
        userDao.queryUserByid(routingContext,id);
    }
    @Router("/delete")
    public void deleteUserInfo(@Param Integer id){
    }
    @Router("/list")
    public void queryUserlist(RoutingContext routingContext){
        userDao.queryUserAll(routingContext);
    }
}
