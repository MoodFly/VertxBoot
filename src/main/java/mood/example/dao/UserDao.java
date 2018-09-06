package mood.example.dao;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.RoutingContext;
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
    JsonObject config;
    AsyncSQLClient postgreSQLClient;
    @Init
    public void init(){
        config.put("host", "localhost");
        config.put("port", 5432);
        config.put("username", "postgres");
        config.put("password", "root");
        config.put("database", "vertx");
        config.put("charset", "UTF-8");
        config.put("queryTimeout", 20000);
        postgreSQLClient = PostgreSQLClient.createShared(vertx,config);
    }
    private static List<User> dataList=new LinkedList<>();
    public void queryUserAll(RoutingContext routingContext){
        Future.<ResultSet>future(future -> {
            postgreSQLClient.query("SELECT * FROM public.\"User\"",future);
        }).compose(x->{
            if(x.getResults().size()==0){
                return Future.future(future -> {
                    future.complete(new JsonObject().put("success","无数据"));
                    future.succeeded();
                });
            }
            return Future.future(future -> {
                future.complete(new JsonObject().put("data",x.getResults()));
                future.succeeded();
            });
        }).setHandler(z->{
            if (z.succeeded()){
                String str=((JsonObject) z.result()).getJsonArray("data").encode();
                routingContext.response().putHeader("Content-Type", "application/json;charset=UTF-8").end(str,"UTF-8");
            }
        });
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
