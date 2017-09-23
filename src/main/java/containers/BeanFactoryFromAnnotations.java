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

    Parser parser;

    public BeanFactoryFromAnnotations()
    {
        parser = new AnnotationParser();
    }

    public void scan(String packa)
    {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packa))));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        for (Class s : classes) {
            setBean(s);
        }

    }

    private void setBean(Class s) {
        try {
            parser.getBeans(this, s);
        } catch (BeanConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printBean(String key)
    {
        Bean bean;
        if(beans.containsKey(key)) {
            bean = beans.get(key);
            System.out.println("El nombre de la clase es: " + bean.getBeanClass().getSimpleName() + "\n El id de la clase es: " + bean.getName() + "\n El metodo preDestruct es: " + bean.getPreDestruct() + "\n El metodo postConstruct " + bean.getPostConstruct() + "\n Scope:  " + bean.isSingleton());
        }
        else
        {
            System.out.println("mamamos");
        }
    }

}
