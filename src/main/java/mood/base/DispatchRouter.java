package mood.base;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import mood.annotation.Router;
import mood.repository.ControllerInfoMapping;
import mood.repository.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public class DispatchRouter {

    public static void dispath(RoutingContext m) {
        Function<RoutingContext,Void> function= x ->{
            Map<String, ControllerInfoMapping> map=null;
            switch(x.request().rawMethod()){
                case "GET" : map=Repository.getGetMappings();break;
                case "PUT" : map=Repository.getPutMappings();break;
                case "POST" : map=Repository.getPostMappings();break;
                case "DELETE" : map=Repository.getDeleteMappings();break;
            }
            String url=x.request().uri();
            String[] getParam=null;
            if (url.indexOf("?")!=-1){
                String urlparam=url.substring(url.indexOf("?")+1,url.length());
                getParam=urlparam.split("&");
            }else {
                getParam=new String[0];
            }
            Map<String,Object> parmmap=new HashMap<>();
            for (String val:getParam){
                parmmap.putIfAbsent(val.split("=")[0],val.split("=")[1]);
            }
            List<Object> paramValue=new ArrayList<>();
            paramValue.add(x);
            for (Map.Entry val:parmmap.entrySet()){
                paramValue.add(val.getValue());
            }
            for(Map.Entry<String,ControllerInfoMapping> entry:map.entrySet()){
                if (url.indexOf((entry.getValue().getUrl()))!=-1){
                    String subUrl="";
                    if (url.indexOf("?")!=-1){
                        subUrl=url.split((entry.getValue().getUrl()))[1].substring(0,url.split((entry.getValue().getUrl()))[1].indexOf("?"));
                    }else{
                        subUrl=url.split((entry.getValue().getUrl()))[1];
                    }
                    Object object=Repository.getVertxBean(entry.getValue().getClassName());
                    Method[] methods = object.getClass().getMethods();
                    for (Method method : methods) {
                        if(method.getAnnotation(Router.class) != null&&method.getAnnotation(Router.class).value().equals(subUrl)) {
                            method.setAccessible(true);
                            try {
                                method.invoke(object,paramValue.toArray());
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
            return null;
        };
        function.apply(m);
    }
}
