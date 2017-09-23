package tests;

import containers.BeanFactory;
import containers.BeanFactoryFromAnnotations;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import parsers.BeanConfigurationException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws BeanConfigurationException, ClassNotFoundException, IOException, NoSuchFieldException {
        BeanFactoryFromAnnotations prueba = new BeanFactoryFromAnnotations();
        prueba.scan("tests");
        prueba.printBean("HOLAAA");
    }
}
