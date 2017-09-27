package containers;

import parsers.BeanConfigurationException;
import parsers.Parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractBeanFactory implements BeanFactory {

    protected Parser parser;
    protected HashMap<String, Bean> beans;

    public AbstractBeanFactory() {
        beans = new HashMap<>();
    }

    public void addBean(String key, Bean bean) throws BeanConfigurationException{
        if (beans.containsKey(key)) {
            throw new BeanConfigurationException("Multiple definitions for bean: "+key);
        } else {
            beans.put(key, bean);
        }
    }

    public String printBean(String key) {
        return beans.get(key).toString();
    }

    public String printBeans() {
        String str = "";
        for(Object bean : beans.values()) {
            str += bean.toString();
        }
        return str;
    }

    //TODO bean injection
    public Object getBean(String name) throws BeanConfigurationException{

        Bean bean;
        Object b = null;
        if((bean = beans.get(name)) == null)
            throw new BeanConfigurationException("Undefined bean \""+name+"\".");

        // if singleton
        if(bean.isSingleton())
            return bean.getSingletonInstance();

        // if prototype
        // prepare dependencies
        LinkedList<Object> dependencies = new LinkedList<>();
        LinkedList<Class> dependencyTypes = new LinkedList<>();
        for(Property prop : bean.getProperties()) {
            // get instance of dependency
            if(prop.getRef() != null) {
                // recursive
                dependencies.add(getBean(prop.getRef()));
            } else {
                dependencies.add(prop.getInstance());
            }
            // get type of dependency
            dependencyTypes.add(prop.getClass());
        }


        // scope is prototype, instantiate new bean
        if(bean.getInjectionType() == 'c') {
            // constructor injection
            Constructor cons = null;
            try {
                cons = bean.getBeanClass().getDeclaredConstructor((Class<?>[]) dependencyTypes.toArray());
            } catch (NoSuchMethodException e) {
                throw new BeanConfigurationException("", e);
            }

            try {
                b = cons.newInstance(dependencies.toArray());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            // setter injection

        }



        return b;
    }
}
