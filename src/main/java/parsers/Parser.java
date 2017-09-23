package parsers;

import containers.AbstractBeanFactory;
import containers.BeanFactory;

public interface Parser {
    void getBeans(AbstractBeanFactory bf, Class s) throws BeanConfigurationException, ClassNotFoundException;
}
