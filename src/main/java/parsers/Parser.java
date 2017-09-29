package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import containers.BeanFactory;

import java.util.HashMap;

public interface Parser {
    void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException;
}
