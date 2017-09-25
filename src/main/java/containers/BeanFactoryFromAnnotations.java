package containers;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import parsers.AnnotationParser;
import parsers.BeanConfigurationException;
import parsers.Parser;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BeanFactoryFromAnnotations extends AbstractBeanFactory {

    public BeanFactoryFromAnnotations() {
        super();
    }

    public void scan(String pack) throws BeanConfigurationException {
        parser = new AnnotationParser(pack);
        parser.getBeans(this);
    }

}
