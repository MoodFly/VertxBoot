package mood.example.router;

import mood.annotation.*;
import mood.example.entity.User;
import mood.example.service.UserService;

@Component(singleton = true)
@Router("/user")
public class UserRouter {

    @AutoWired
    private UserService service;
    @Router(value = "/save",Method = "POST")
    public void saveUserInfo(@Param String name,@Param String job,@Param String sex){
        service.saveUser(new User(name,job,sex));
    }
    @Router(value = "/update",Method = "POST")
    public void updateUserInfo(@Param Integer id,@Param String name,@Param String job,@Param String sex){
        service.updayeUser(new User(id,name,job,sex));
    }
    @Router("/{id}")
    public void queryUserInfo(@Path Integer id){
        service.queryUserById(id);
    }
    @Router("/delete")
    public void deleteUserInfo(@Param Integer id){
        service.deleteUser(new User(id));
    }
    @Router("/list")
    public void queryUserlist(){
        service.queryUserAll();
    }
}
