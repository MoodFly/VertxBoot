package mood.example.dao;

import mood.annotation.*;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import mood.example.entity.User;

import java.util.*;
@Component
public class UserDao {
    @AutoWired
    Vertx vertx;
    JDBCClient client;
    @Value
    Date date;
    @Value
    HashMap config;
    @Init
    public void init(){
        client=JDBCClient.createShared(vertx,JsonObject.mapFrom(config),"myDataSource");
    }
    private static List<User> dataList=new LinkedList<>();
    static{
        dataList.add(new User(1,"王五","工程师","男"));
        dataList.add(new User(2,"李四","数学家","男"));
        dataList.add(new User(3,"张三","工程师","女"));
        dataList.add(new User(4,"流浪","工程师","男"));
        dataList.add(new User(5,"亚索","工程师","女"));
        dataList.add(new User(6,"鱼人","工程师","男"));
        dataList.add(new User(7,"盖伦","工程师","女"));
        dataList.add(new User(8,"盲僧","工程师","男"));
    }
    public List<User> queryUserAll(){
        return dataList;
    }
    public User queryUserById(int id){
        return dataList.stream().filter(x->x.getId().equals(id)).findFirst().get();
    }
    public int saveUser(User user){
        Integer id=dataList.stream().sorted(Comparator.comparing(User::getId))
                .map(x->x.getId()).findFirst().get();
        user.setId(id+1);
        dataList.add(user);
        return user.getId();
    }
    public int updayeUser(User user){
        dataList.forEach(x->{
            if (x.getId()==user.getId()){
                x.setName(user.getName());
                x.setJob(user.getJob());
                x.setSex(user.getSex());
            }
        });
        return user.getId();
    }
    public int deleteUser(User user){
        Iterator<User> iterable=dataList.iterator();
       while (iterable.hasNext()){
           if (iterable.next().getId()==user.getId()){
               iterable.remove();
           }
       }
       return dataList.size();
    }

}
