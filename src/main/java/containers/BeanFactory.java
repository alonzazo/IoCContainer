package containers;
import exceptions.BeanConfigurationException;
import exceptions.BeanInstantiationException;

public interface BeanFactory {
    void scan(String s) throws BeanConfigurationException;
    void addBean(String name, Bean bean) throws BeanConfigurationException;
    Object getBean(String name) throws BeanInstantiationException;
    String printBean(String key);
    String printBeans();
    void close() throws BeanInstantiationException;
}
