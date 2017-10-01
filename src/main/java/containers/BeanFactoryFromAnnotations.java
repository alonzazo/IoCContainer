package containers;

import parsers.AnnotationParser;
import exceptions.BeanConfigurationException;


public class BeanFactoryFromAnnotations extends AbstractBeanFactory {

    public BeanFactoryFromAnnotations() {
        super();
    }

    public void scan(String pack) throws BeanConfigurationException {
        parser = new AnnotationParser(pack);
        parser.getBeans(this);
        if(dependencyCycle())
        {
            throw new BeanConfigurationException("Impossible to resolve bean dependencies, a cycle is formed.");
        }
    }

}
