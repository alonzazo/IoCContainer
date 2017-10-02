package containers;
import exceptions.BeanConfigurationException;
import exceptions.BeanInstantiationException;

public interface BeanFactory {
    void scan(String s) throws BeanConfigurationException;
    Object getBean(String name) throws BeanInstantiationException;
    void close() throws BeanInstantiationException;
}
