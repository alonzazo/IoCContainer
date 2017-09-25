package containers;
import parsers.BeanConfigurationException;

public interface BeanFactory {
    void scan(String s) throws BeanConfigurationException;
    void addBean(String name, Bean bean) throws BeanConfigurationException;
    Object getBean(String name);
    String printBean(String key);
    String printBeans();
}
