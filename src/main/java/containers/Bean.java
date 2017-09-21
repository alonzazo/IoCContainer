package containers;

import java.util.LinkedList;

public class Bean {

    private LinkedList<Property> properties;
    private Object singletonInstance;
    private String name;
    private Boolean singleton;
    private Class beanClass;
    private String postConstruct;
    private String preDestruct;
    private String targetSetter;

    public Bean() {}

    public Bean(String n, Boolean single, Class bClass, String post, String pre, String setter) {
        name = n;
        singleton = single;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        targetSetter = setter;
    }

    public void setName(String n) {
        name = n;
    }

    public void setSingleton(Boolean singleton) {
        this.singleton = singleton;
    }

    public void setClass(Class bClass) {
        beanClass = bClass;
    }

    public void setPostConstruct(String post) {
        postConstruct = post;
    }

    public void setPreDestruct(String pre) {
        preDestruct = pre;
    }

    public void setTargetSetter(String setter) {
        targetSetter = setter;
    }

    public String getName() {
        return name;
    }

    public Boolean isSingleton() {
        return singleton;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public String getPostConstruct() {
        return postConstruct;
    }

    public String getPreDestruct() {
        return preDestruct;
    }

    public String getTargetSetter() {
        return targetSetter;
    }
}
