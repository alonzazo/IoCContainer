package tests;

import containers.BeanFactory;
import containers.BeanFactoryFromAnnotations;
import containers.BeanFactoryFromXML;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import parsers.BeanConfigurationException;
import parsers.XMLParser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws BeanConfigurationException, ClassNotFoundException, IOException, NoSuchFieldException {
        System.out.println("ANNOTATIONS:");
        BeanFactory annoBF = new BeanFactoryFromAnnotations();
        try {
            annoBF.scan("tests");
        } catch (BeanConfigurationException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println(annoBF.printBeans()+"\n\n");

        Hey hey = (Hey) annoBF.getBean("heyBean");

        /*System.out.println("XML:");
        BeanFactory xmlBF = new BeanFactoryFromXML();

        // EL XML ESTA EN RESOURCES, CAMBIEN EL PATH PARA PODER TESTEAR
        xmlBF.scan("src\\main\\resources\\UNIQUENAME.xml");
        System.out.println(xmlBF.printBeans());

        Hey hey = (Hey) xmlBF.getBean("heyBean");

        hey.funciono();
        System.out.println(hey.testSingleton);
        hey.changeTestSingleton();

        Hey hey2 = (Hey) xmlBF.getBean("heyBean");
        System.out.println(hey2.testSingleton);

        System.out.println("SE VA A CERRAR EL CONTAINER");

        xmlBF.close();

        System.out.println("SE CERRó EL CONTAINER");

        try {
            hey = (Hey) xmlBF.getBean("heyBean");
        } catch(BeanConfigurationException e) {
            System.out.println("FUNCIONA CLOSE");
        }*/
    }
}
