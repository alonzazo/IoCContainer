package containers;

import parsers.BeanConfigurationException;
import parsers.Parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            throw new BeanConfigurationException("Multiple definitions of bean: \""+key+"\".");
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

    public Object getBean(String name) throws BeanConfigurationException{

        Bean bean, dependency;
        Object newInstance = null;
        Class[] size;
        if((bean = beans.get(name)) == null)
            throw new BeanConfigurationException("Undefined bean \""+name+"\".");

        // if singleton and has been initialized
        if(bean.isSingleton() && bean.getSingletonInstance() != null)
            return bean.getSingletonInstance();

        // if prototype or singleton has not been initialized
        // prepare dependencies
        LinkedList<Object> dependencies = new LinkedList<>();
        for(Property prop : bean.getProperties()) {
            // get instance of dependency
            if(prop.getRef() != null) {
                // recursive
                dependencies.add(getBean(prop.getRef()));
            } else {
                dependencies.add(prop.getInstance());
            }
        }

        // instantiate new bean, with methods set by getInjectors
        if(bean.getInjectionType() == 'c') {
            // constructor injection
            try {
                newInstance = bean.getConstructor().newInstance(dependencies.toArray());
            } catch (InstantiationException e) {
                throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
            } catch (IllegalAccessException e) {
                throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
            } catch (InvocationTargetException e) {
                throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
            }
        } else {
            // setter injection
            try {
                newInstance = bean.getBeanClass().newInstance();
            } catch (InstantiationException e) {
                throw new BeanConfigurationException("No nullary constructor found for bean \""+bean.getName()+"\" of class \""+bean.getBeanClass().getName()+"\".", e);
            } catch (IllegalAccessException e) {
                throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
            }

            for(int i = 0; i < dependencies.size(); i++) {
                try {
                    bean.getSetter(bean.getProperties().get(i).getName()).invoke(newInstance,dependencies.get(i));
                } catch (IllegalAccessException e) {
                    throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
                } catch (InvocationTargetException e) {
                    throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
                }
            }
        }

        // run post construction method if necessary
        if(bean.getPostConstruct() != null) {
            try {
                bean.getPostConstruct().invoke(newInstance);
            } catch (IllegalAccessException e) { //TODO ERROR MESSAGES
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        // if initializing singleton for the first time, set the instance
        if(bean.isSingleton()) {
            bean.setSingletonInstance(newInstance);
        }

        return newInstance;
    }

     public void close() {

        for(Bean bean : beans.values()) {
            if(bean.isSingleton() && bean.getPreDestruct() != null) {
                try {
                    bean.getPreDestruct().invoke(bean.getSingletonInstance());
                } catch (IllegalAccessException e) { //TODO ERROR MESSAGES
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        beans.clear();
    }

    private void setDependencies() {

    }

}
