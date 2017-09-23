package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mariana on 20/09/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Autowired {
    boolean required = false;
     String value() default "byType";;
    Autowire autowiring = Autowire.byType;

}
enum Autowire {
   byType, byName
}
