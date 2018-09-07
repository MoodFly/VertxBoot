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

    public void queryUserByid(RoutingContext routingContext, String id) {
        Future.<ResultSet>future(future -> {
            postgreSQLClient.query("SELECT * FROM public.\"User\" where id="+id+"",future);
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
}
