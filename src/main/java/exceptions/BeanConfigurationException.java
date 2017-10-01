package exceptions;

/*
 * Handles exceptions when parsing bean configuration from both XML and Annotations
 */

public class BeanConfigurationException extends Exception {

    public BeanConfigurationException(String message){
        super(message);
    }

    public BeanConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanConfigurationException(Throwable cause) {super(cause);}

}
