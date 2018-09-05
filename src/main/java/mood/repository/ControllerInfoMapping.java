package mood.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

 /**
 * <p>Description: mood-vertx-repository ControllerInfoMapping</p>*
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
  *@Description: URL-Class-Method-Param
 * @version: 1.0
 */
@Getter
@Setter
@ToString
public final class ControllerInfoMapping {

    private String url;
    
    private String className;
    
    private String classMethod;
    
    private List<ControllerMappingParameter> parameters = new ArrayList<>();
    /**
     * 是否单例
     */
    private boolean singleton;

     public String getUrl() {
         return url;
     }

     public String getClassName() {
         return className;
     }

     public String getClassMethod() {
         return classMethod;
     }

     public List<ControllerMappingParameter> getParameters() {
         return parameters;
     }

     public boolean isSingleton() {
         return singleton;
     }

     public void setUrl(String url) {
         this.url = url;
     }

     public void setClassName(String className) {
         this.className = className;
     }

     public void setClassMethod(String classMethod) {
         this.classMethod = classMethod;
     }

     public void setParameters(List<ControllerMappingParameter> parameters) {
         this.parameters = parameters;
     }

     public void setSingleton(boolean singleton) {
         this.singleton = singleton;
     }
 }
