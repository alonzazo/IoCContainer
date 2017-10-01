package containers;

import parsers.AnnotationParser;
import parsers.BeanConfigurationException;


public class BeanFactoryFromAnnotations extends AbstractBeanFactory {

    public BeanFactoryFromAnnotations() {
        super();
    }

    public void scan(String pack) throws BeanConfigurationException {
        parser = new AnnotationParser(pack);
        parser.getBeans(this);
    }

}
