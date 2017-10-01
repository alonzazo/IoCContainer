package containers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.lang.reflect.Method;

public class Bean {

    private boolean byName;
    private boolean singleton;
    private char injectionType;
    private String name;
    private Object singletonInstance;
    private Class beanClass;
    private Constructor constructor;
    private Method postConstruct;
    private Method preDestruct;
    private ArrayList<Property> properties ;
    private ArrayList<Method> setters;

    public Bean(String n, char injection, Boolean single, boolean by,Class bClass, Method post, Method pre,ArrayList<Property> props) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        byName = by;
        properties = new ArrayList<>(props);
        setters = new ArrayList<>();
    }

    public Bean(String n, char injection, Boolean single, boolean by,Class bClass, Method post, Method pre,ArrayList<Property> props, Constructor c, ArrayList<Method> setts) {
        name = n;
        singleton = single;
        injectionType = injection;
        beanClass = bClass;
        postConstruct = post;
        preDestruct = pre;
        byName = by;
        properties = new ArrayList(props);
        constructor = c;
        setters = new ArrayList<>(setts);
    }


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


    public ArrayList<Method> getSetters() {
        return setters;
    }

    public void setSetters(ArrayList<Method> setters) {
        this.setters = setters;
    }

    public void addSetter(Method m){
        setters.add(m);
    }

    public Method getSetter(int i ){
        return setters.get(i);
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

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public String toString() {
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
    }
}
