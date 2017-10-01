package exceptions;

public class BeanInstantiationException extends Exception {
    public BeanInstantiationException(String message) {super(message);}
    public BeanInstantiationException(Throwable cause) {super(cause);}
    public BeanInstantiationException(String message, Throwable cause) {super(message, cause);}
}
