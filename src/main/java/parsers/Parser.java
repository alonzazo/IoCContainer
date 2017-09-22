package parsers;

import containers.BeanFactory;

public interface Parser {
    void getBeans(BeanFactory bf) throws BeanConfigurationException, ClassNotFoundException;
}
