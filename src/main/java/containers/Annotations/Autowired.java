package containers.Annotations;

/**
 * Created by Mariana on 20/09/2017.
 */
public @interface Autowired {
    boolean required = false;
    Autowire autowiring = Autowire.byType;
}
enum Autowire {
   byType, byName
}
