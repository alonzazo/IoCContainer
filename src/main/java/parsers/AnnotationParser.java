package parsers;

import containers.Bean;
import containers.BeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationParser implements Parser {

    private String pack;


    public AnnotationParser()
    {

    }

    public AnnotationParser(String pack)
    {
        this.pack = pack;
    }
    public void getBeans(BeanFactory bf) throws ClassNotFoundException {
        Bean bean;
        String id, postCons = null, preDes = null, setter = null;
        Boolean isSingleton = null;
        Class beanClass;
        beanClass = getClass(pack);
        id = beanClass.getSimpleName();
        Annotation[] annos = beanClass.getAnnotations();
        if (annos.length != 0 && annos[0].annotationType().getSimpleName().equals("Component")) {
            System.out.println("FUNCIONA MIERDA");
            if (annos.length >= 2 && annos[1].annotationType().getSimpleName().equals("Scope")) {
                String[] parametter = annos[1].toString().split("\"");
                switch (parametter[0]) {
                    case "Prototype":
                        isSingleton = false;
                        break;
                    case "Singleton":
                    default:
                        isSingleton = true;
                        break;
                }
            } else {
                isSingleton = true;
            }
            Method[] methods = getMethods(beanClass);
            for (Method method : methods) {
                Annotation[] methodAnnos = method.getAnnotations();
                for (Annotation an : methodAnnos) {
                    String type = an.annotationType().getSimpleName();
                    switch (type) {
                        case "Autowired":
                            if (method.getName().contains("set")) {
                                setter = method.getName();
                            } else {
                                setter = null;
                            }
                            break;
                        case "PostInicialization":
                            postCons = method.getName();
                            break;
                        case "PreDestruction":
                            preDes = method.getName();
                            break;
                    }
                }
            }
        }
        else
        {
            System.out.println("NO ES BEAN");
        }
        bean = new Bean(id, isSingleton, beanClass, postCons, preDes,setter);
        bf.addBean(id, bean);
    }

    private Method[] getMethods(Class c)
    {
        return c.getMethods();
    }

    private Class getClass(String classpack) throws ClassNotFoundException {
        return Class.forName(classpack);
    }

}
