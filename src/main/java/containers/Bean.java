package containers;

import java.util.LinkedList;

public class Bean {

    private LinkedList<Property> properties;
    private Object singletonInstance;
    private String name;
    private Boolean singleton;
    private char injectionType;
    private Class beanClass;
    private String postConstruct;
    private String preDestruct;

    public Bean() {}

    public Bean(String n, char injection, Boolean single, Class bClass, String post, String pre, LinkedList<Property> props) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        properties = new LinkedList<Property>(props);
    }

    public Bean(String n, char injection, Boolean single, Class bClass, String post, String pre) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        properties = new LinkedList<Property>();
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

    public void setInjectionType(char injectionType) {
        this.injectionType = injectionType;
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

    public char getInjectionType() {
        return injectionType;
    }

    public String toString() {
        String str = "Bean name: "+name+"\nType: "+beanClass+"\nScope: ";
        if(isSingleton()) {
            str+="singleton";
        } else {
            str+="prototype";
        }
        str += "\nInjection type: ";
        if(injectionType=='c') {
            str+="constructor";
        } else if(injectionType=='s') {
            str+="setter";
        }
        str += "\npostConstructor: "+postConstruct+"\npreDestruct: "+preDestruct;

        if(!properties.isEmpty()) {
            str+="\nProperties:\n";
            for (Property prop: properties) {
                str += prop.toString()+"\n\n";
            }
        }
        return str;
    }
}
