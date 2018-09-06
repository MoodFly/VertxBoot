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
    }
    @Router(value = "/update",Method = "POST")
    public void updateUserInfo(@Param Integer id,@Param String name,@Param String job,@Param String sex){
    }
    @Router("/{id}")
    public void queryUserInfo(@Path Integer id){
    }
    @Router("/delete")
    public void deleteUserInfo(@Param Integer id){
    }
    @Router("/list")
    public void queryUserlist(){
    }
}
