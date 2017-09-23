package tests;

import containers.Bean;
import containers.BeanFactory;
import containers.BeanFactoryFromAnnotations;
import parsers.AnnotationParser;
import parsers.BeanConfigurationException;
import parsers.Parser;

import java.lang.annotation.Annotation;

public class Main {
    public static void main(String[] args) throws BeanConfigurationException, ClassNotFoundException {
        /*BeanFactoryFromAnnotations an = new BeanFactoryFromAnnotations();
        an.scan(".\\target\\classes\\containers");*/
        BeanFactory bf = new BeanFactoryFromAnnotations();
        Parser parser = new AnnotationParser("tests.Hey");
        parser.getBeans(bf);
        /*Class c = Class.forName("tests.Hey");
        System.out.println(c.getName());
        Annotation[] an = c.getAnnotations();
        System.out.println(an[0].annotationType().getSimpleName());*/
        //System.out.println(an.annotationType().getSimpleName());





    }
}
