package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import containers.Property;
import exceptions.BeanConfigurationException;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AnnotationParser extends AbstractParser {
    private Set<Class<?>> classes;
    private ArrayList<Property> properties;

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
        properties = new ArrayList<>();
    }

    public void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException {
        Bean bean;
        String id = null, param = null;
        Method postCons = null, preDes = null;
        char injectionType = ' ';
        Boolean isSingleton = null;
        Boolean byName = null;
        Property property;
        Annotation[] annos, consAn;
        Class[] types;
        String params[], type;
        Constructor beanCons = null;
        ArrayList<Method> setters = new ArrayList<>();
        LinkedList<Class> propertyTypes = new LinkedList<>(); // to check there are no repeated property types when autowiring by type
        LinkedList<String> propertyNames = new LinkedList<>(); // to check there are no repeated property names when autowiring by name

        for (Class c : classes) {
            id = null;
            postCons = null;
            preDes = null;
            injectionType = ' ';
            properties.clear();
            propertyTypes.clear();
            propertyNames.clear();
            setters.clear();
            annos = c.getAnnotations();
            if (annos.length != 0 && annos[0].annotationType().getSimpleName().equals("Component")) {
                params = annos[0].toString().split("=");
                if(params[1].length() > 1)
                {
                    id = params[1].substring(0,params[1].length()-1);
                }
                else {
                    id = c.getSimpleName();
                }

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
                            throw new BeanConfigurationException("Unrecognized scope \""+param+"\" in bean \""+id+"\".");
                    }
                } else {
                    isSingleton = true;
                }
                for (Method method : c.getMethods()) {
                    for (Annotation an : method.getAnnotations()) {
                        type = an.annotationType().getSimpleName();
                        switch (type) {
                            case "Autowired":
                                if (method.getName().contains("set")) {
                                    injectionType = 's';

                                    if(method.getParameterCount() != 1)
                                        throw new BeanConfigurationException("Setter method annotated with @Autowired \""+method.getName()+"\" in class \""+c.getName()+"\" must have exactly one parameter.");


                                    try {
                                        beanCons = c.getConstructor();
                                    } catch (NoSuchMethodException e) {
                                        throw new BeanConfigurationException("Class \""+c.getName()+"\" of bean \""+id+"\" must have a nullary constructor.",e);
                                    }
                                    params = an.toString().split("=");
                                    if (params[1].length() > 1) { //byName
                                        byName = true;
                                        types = method.getParameterTypes();

                                        property = new Property();
                                        property.setName(params[1].substring(0,params[1].length()-1));

                                        if(propertyNames.contains(property.getName()))
                                            throw new BeanConfigurationException("Class \""+c.getName()+"\" cannot have multiple setter methods annotated with @Autowired  for the property \""+property.getName()+"\".");
                                        propertyNames.add(property.getName());

                                        property.setRef(params[1].substring(0,params[1].length()-1));
                                        property.setType(types[0]);
                                        properties.add(property);
                                        setters.add(method);
                                    } else {  //byType
                                        byName = false;
                                        types = method.getParameterTypes();
                                        if(propertyTypes.contains(types[0]))  // there is already a property of that type
                                            throw new BeanConfigurationException("Class \""+c.getName()+"\" cannot have multiple setter methods annotated with @Autowired for the type \""+types[0].getName()+"\".");
                                        propertyTypes.add(types[0]);

                                        // instantiate property
                                        property = new Property();
                                        property.setRef(types[0].getSimpleName());
                                        property.setName(types[0].getSimpleName());
                                        property.setType(types[0]);
                                        properties.add(property);

                                        setters.add(method);
                                    }
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

                // look for constructors with @Autowired
                // even if already found setters, for exception handling purposes
                for(Constructor con : c.getConstructors())
                {
                    consAn = con.getDeclaredAnnotations();
                    if(consAn.length != 0 && consAn[0].annotationType().getSimpleName().equals("Autowired"))
                    {
                        if( consAn[0].toString().split("=")[1].length() > 1)
                            throw new BeanConfigurationException("@Autowired annotation in bean \""+id+"\" of class \""+c.getName()+"\" cannot have parameters on a constructor.");

                        if(injectionType == 'c') // already set as constructor, more than one constructor has @Autowired
                            throw new BeanConfigurationException("Only one constructor may be annotated with @Autowired in bean \""+id+"\" of class \""+c.getName()+"\".");

                        if(injectionType == 's') // already set as setter
                            throw new BeanConfigurationException("Cannot annotate with @Autowired both constructors and setter methods in bean \""+id+"\" of class \""+c.getName()+"\".");

                        injectionType = 'c';
                        byName = false;
                        beanCons = con;
                        for(Class conType : con.getParameterTypes())
                        {
                            if(propertyTypes.contains(conType))  // there is already a property of that type
                                throw new BeanConfigurationException("Class \""+c.getName()+"\" cannot have a constructor annotated with @Autowired with multiple parameters of the type \""+conType.getName()+"\".");
                            propertyTypes.add(conType);

                            property = new Property();
                            property.setRef(conType.getSimpleName());
                            property.setName(conType.getSimpleName());
                            property.setType(conType);
                            properties.add(property);
                        }
                    }
                }

                // found no methods/constructors with @Autowired
                if (injectionType == ' ') {
                    try {
                        beanCons = c.getConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new BeanConfigurationException("Class \""+c.getName()+"\" of bean \""+id+"\" must have a nullary constructor.",e);
                    }
                    byName = false;
                }

                bean = new Bean(id, injectionType, isSingleton, byName, c, postCons, preDes, properties, beanCons, setters);
                bf.addBean(id, bean);
            }
        }
    }
}
