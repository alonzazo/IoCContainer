package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import exceptions.BeanConfigurationException;

import java.util.HashMap;

public interface Parser {
    void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException;
    void getInjectors(HashMap<String,Bean> beans) throws BeanConfigurationException;
}
