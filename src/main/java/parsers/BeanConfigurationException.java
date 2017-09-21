package parsers;

/*
 * Handles exceptions when parsing bean configuration from both XML and Annotations
 */

public class BeanConfigurationException extends Exception {

    public BeanConfigurationException(String message){
        super("Bean configuration exception: "+message);
    }

    public BeanConfigurationException(String message, Throwable cause) {
        super("Bean configuration exception: "+message, cause);
    }

}
