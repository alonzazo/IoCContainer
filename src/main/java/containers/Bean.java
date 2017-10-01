package containers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.reflect.Method;

public class Bean {

    private LinkedList<Property> properties ;
    private Object singletonInstance;
    private String name;
    private boolean singleton;
    private char injectionType;
    private Class beanClass;
    private Method postConstruct;
    private Method preDestruct;
    private HashMap<String,Method> setters;
    private boolean byName;
    private Constructor constructor;

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }


    public boolean isByName() {
        return byName;
    }

    public void setByName(boolean byName) {
        this.byName = byName;
    }

    public Bean() {}

    public Bean(String n, char injection, Boolean single, Class bClass, Method post, Method pre, LinkedList<Property> props) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        properties = new LinkedList(props);
        setters = new HashMap<>();
    }

    public Bean(String n, char injection, Boolean single, Class bClass, Method post, Method pre) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        properties = new LinkedList();
        setters = new HashMap<>();
    }

    public Bean(String n, char injection, Boolean single, boolean by,Class bClass, Method post, Method pre,LinkedList<Property> props) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        byName = by;
        properties = new LinkedList(props);
        setters = new HashMap<>();
    }

    public Bean(String n, char injection, Boolean single, boolean by,Class bClass, Method post, Method pre,LinkedList<Property> props, Constructor c) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        byName = by;
        properties = new LinkedList(props);
        setters = new HashMap<>();
        constructor = c;
    }

    public HashMap<String, Method> getSetters() {
        return setters;
    }

    public void setSetters(HashMap<String, Method> setters) {
        this.setters = setters;
    }

    public void addSetter(String s,Method m){
        setters.put(s,m);
    }

    public Method getSetter(String s){
        return setters.get(s);
    }

    public void setName(String n) {
        name = n;
    }

    public void setSingletonInstance(Object singletonInstance) {
        this.singletonInstance = singletonInstance;
    }

    public void setSingleton(Boolean singleton) {
        this.singleton = singleton;
    }

    public void setClass(Class bClass) {
        beanClass = bClass;
    }

    public void setPostConstruct(Method post) {
        postConstruct = post;
    }

    public void setPreDestruct(Method pre) {
        preDestruct = pre;
    }

    public void setInjectionType(char injectionType) {
        this.injectionType = injectionType;
    }

    public String getName() {
        return name;
    }

    public Object getSingletonInstance() {
        return singletonInstance;
    }

    public Boolean isSingleton() {
        return singleton;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public Method getPostConstruct() {
        return postConstruct;
    }

    public Method getPreDestruct() {
        return preDestruct;
    }

    public char getInjectionType() {
        return injectionType;
    }

    public LinkedList<Property> getProperties() {
        return properties;
    }

    public void setProperties(LinkedList<Property> properties) {
        this.properties = properties;
    }

    /*public String toString() {
        String str = "Bean name: "+name+"\nType: "+beanClass.getName()+"\nScope: ";
        if(singleton) {
            str+="singleton";
        } else {
            str+="prototype";
        }
        str += "\nAutowiring mode: ";
        if(byName) {
            str+="byName";
        } else {
            str+="byType";
        }
        str += "\nInjection type: ";
        if(injectionType=='c') {
            str+="constructor";
        } else if(injectionType=='s') {
            str+="setter";
        }
        str += "\npostConstructor: "+postConstruct+"\npreDestruct: "+preDestruct + "\n";

        if(!properties.isEmpty()) {
            str+="\nProperties:\n";
            for (Property prop: properties) {
                str += prop.toString()+"\n\n";
            }
        }
        return str;
    }*/
}
