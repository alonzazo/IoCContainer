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
        LinkedList<Class> dependencyTypes = new LinkedList<>();
        for(Property prop : bean.getProperties()) {
            // get instance of dependency
            if(prop.getRef() != null) {
                // recursive
                dependencies.add(getBean(prop.getRef()));
                dependencyTypes.add(beans.get(prop.getRef()).getBeanClass());
            } else {
                dependencies.add(prop.getInstance());
                dependencyTypes.add(prop.getType());
            }
        }

        // instantiate new bean
        if(bean.getInjectionType() == 'c') {
            // constructor injection
            Constructor cons = null;
            try {
                size = new Class[dependencyTypes.size()];
                cons = bean.getBeanClass().getDeclaredConstructor((Class[]) dependencyTypes.toArray(size));
            } catch (NoSuchMethodException e) {
                throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
            }

            try {
                newInstance = cons.newInstance(dependencies.toArray());
            } catch (InstantiationException e) { //TODO ERROR MESAGES
                throw new BeanConfigurationException("", e);
            } catch (IllegalAccessException e) {
                throw new BeanConfigurationException("", e);
            } catch (InvocationTargetException e) {
                throw new BeanConfigurationException("", e);
            }

        } else {
            // TODO setter injection
            try {
                newInstance = bean.getBeanClass().newInstance();
            } catch (InstantiationException e) { //TODO ERROR MESSAGE
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < dependencyTypes.size(); i++) {

                Method injector = bean.getInjector(dependencyTypes.get(i));
                try {
                    injector.invoke(newInstance, dependencies.get(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
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
