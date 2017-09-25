package containers;

import parsers.BeanConfigurationException;
import parsers.Parser;

import java.util.HashMap;

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
    public Object getBean(String name){return new Object();}
}
