package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import containers.BeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationParser implements Parser {

    public AnnotationParser()
    {

    }
    public void getBeans(AbstractBeanFactory bf, Class c) throws ClassNotFoundException {
        Bean bean;
        String id = null, postCons = null, preDes = null, setter = null;
        Boolean isSingleton = null;
        Annotation[] annos = c.getAnnotations();
        if (annos.length != 0 && annos[0].annotationType().getSimpleName().equals("Component")) {
            if (annos.length >= 2 && annos[1].annotationType().getSimpleName().equals("Scope")) {
                String[] parameter = annos[1].toString().split("=");
                switch (parameter[1].substring(0,parameter[1].length()-1)) {
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
            Method[] methods = getMethods(c);
            for (Method method : methods) {
                Annotation[] methodAnnos = method.getAnnotations();
                for (Annotation an : methodAnnos) {
                    String type = an.annotationType().getSimpleName();
                    switch (type) {
                        case "Autowired":
                            String[] parameter = an.toString().split("=");
                            if(parameter.length != 0 && !parameter[1].substring(0,parameter[1].length()-1).equals("byType"))
                            {
                                id = parameter[1].substring(0,parameter[1].length()-1);
                            }
                            else
                            {
                                id = c.getSimpleName();
                            }
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
            bean = new Bean(id, isSingleton, c, postCons, preDes,setter);
            System.out.println(id);
            bf.addBean(id, bean);
        }
        else {
            System.out.println("NO ES BEAN");
        }
    }

    private Method[] getMethods(Class c)
    {
        return c.getMethods();
    }

}
