package containers;

import exceptions.BeanConfigurationException;
import parsers.XMLParser;

public class BeanFactoryFromXML extends AbstractBeanFactory {

    public BeanFactoryFromXML() {
        super();
    }

    public void scan(String s) throws BeanConfigurationException {
        parser = new XMLParser(s);
        parser.getBeans(this);
        parser.getInjectors(this.beans);
        if(dependencyCycle())
        {
            throw new BeanConfigurationException("Impossible to resolve bean dependencies, a cycle is formed.");
        }
    }

}
