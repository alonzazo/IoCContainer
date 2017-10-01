package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import java.util.HashMap;

public interface Parser {
    void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException;
    public void getInjectors(HashMap<String,Bean> beans) throws BeanConfigurationException;
}
