package mood.repository;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Description: mood-vertx-repository ComponentBean</p>
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 * @version: 1.0
 */
@Setter
@Getter
public final class ComponentBean {
    public ComponentBean(Class<?> clazz, boolean singleton) {
        this.clazz = clazz;
        this.singleton = singleton;
    }
    private Class<?> clazz;
    private boolean singleton;

    public Class<?> getClazz() {
        return clazz;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
}
