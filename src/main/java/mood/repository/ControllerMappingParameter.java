package mood.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Description: mood-vertx-repository ControllerMappingParameter</p>*
 * @author: by Mood
 * @date: 2018-8-30 11:11:11
 *@Description: URL-Class-Method-Param
 * @version: 1.0
 */
@ToString
@Setter
@Getter
public final class ControllerMappingParameter {
    private String name;
    private Class<?> dataType;
    private ControllerMappingParameterTypeEnum type;
    private boolean required = true;

    public String getName() {
        return name;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public ControllerMappingParameterTypeEnum getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    public void setType(ControllerMappingParameterTypeEnum type) {
        this.type = type;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
