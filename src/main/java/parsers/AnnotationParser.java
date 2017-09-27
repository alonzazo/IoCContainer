package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import containers.BeanFactory;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AnnotationParser implements Parser {
    private Set<Class<?>> classes;

    public AnnotationParser(String pack) {
        // stackoverflow
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        // stackoverflow
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pack))));

        classes = reflections.getSubTypesOf(Object.class);
    }

    public void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException {
        Bean bean;
        String id = null, param;
        Method postCons = null, preDes = null;
        char injectionType = ' ';
        Boolean isSingleton = null;
        Annotation[] annos;
        String params[];
        for (Class c : classes) {
            id = c.getSimpleName();
            annos = c.getAnnotations();
            if (annos.length != 0 && annos[0].annotationType().getSimpleName().equals("Component")) {
                if (annos.length >= 2 && annos[1].annotationType().getSimpleName().equals("Scope")) {
                    params = annos[1].toString().split("=");
                    param = params[1].substring(0, params[1].length() - 1);
                    switch (param) {
                        case "Prototype":
                            isSingleton = false;
                            break;
                        case "Singleton":
                            isSingleton = true;
                            break;
                        default:
                            throw new BeanConfigurationException("Unrecognized scope \""+param+"\" in bean LOL.");
                    }
                } else {
                    isSingleton = true;
                }
                Method[] methods = c.getMethods();
                for (Method method : methods) {
                    Annotation[] methodAnnos = method.getAnnotations();
                    for (Annotation an : methodAnnos) {
                        String type = an.annotationType().getSimpleName();
                        switch (type) {
                            case "Autowired":
                                String[] parameter = an.toString().split("=");
                                if (parameter.length != 0 && !parameter[1].substring(0, parameter[1].length() - 1).equals("byType")) {
                                    // autowired's parameters name dependencies, NOT the name of the name
                                    //TODO: save dependencies of the methods, read autowired parameters
                                    //id = parameter[1].substring(0, parameter[1].length() - 1);
                                } else {

                                }
                                if (method.getName().contains("set")) {
                                    injectionType = 's';
                                } else {
                                    injectionType = 'c';
                                }
                                break;
                            case "PostInicialization":
                                postCons = method;
                                break;
                            case "PreDestruction":
                                preDes = method;
                                break;
                        }
                    }
                }
                bean = new Bean(id, injectionType, isSingleton, c, postCons, preDes);
                bf.addBean(id, bean);
            } else {
                // TODO quitar esto
                System.out.println("Class "+c.getName()+" is not a bean.");
            }
        }
    }

    /*private Method[] getMethods(Class c)
    {
        return c.getMethods();
    }*/

}
