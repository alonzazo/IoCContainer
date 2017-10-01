package tests;

import containers.BeanFactory;
import containers.BeanFactoryFromAnnotations;
import exceptions.BeanConfigurationException;
import exceptions.BeanInstantiationException;


public class Main {
    public static void main(String[] args) {
        System.out.println("ANNOTATIONS:");
        BeanFactory annoBF = new BeanFactoryFromAnnotations();
        try {
            annoBF.scan("tests");
        } catch (BeanConfigurationException e) {
            e.printStackTrace();
            System.exit(0);
        }

        Hey hey = null;
        try {
            hey = (Hey) annoBF.getBean("heyBean");
        } catch(BeanInstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        }
        hey.injectThisPls.funciono();
        try {
            annoBF.close();
        } catch (BeanInstantiationException e) {
            e.printStackTrace();
        }

        /*System.out.println("XML:");
        BeanFactory xmlBF = new BeanFactoryFromXML();

        // EL XML ESTA EN RESOURCES, CAMBIEN EL PATH PARA PODER TESTEAR
        try {
            xmlBF.scan("src\\main\\resources\\UNIQUENAME.xml");
        } catch (BeanConfigurationException e) {
            e.printStackTrace();
        }

        Hey hey = null;
        try {
            hey = (Hey) xmlBF.getBean("heyBean");
        } catch (BeanConfigurationException e) {
            e.printStackTrace();
        }

        hey.funciono();
        System.out.println(hey.testSingleton);
        hey.changeTestSingleton();

        Hey hey2 = null;
        try {
            hey2 = (Hey) xmlBF.getBean("heyBean");
        } catch (BeanConfigurationException e) {
            e.printStackTrace();
        }
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
