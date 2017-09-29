package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import containers.Property;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractParser implements Parser{

    public void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException{}
    public void getInjectors(HashMap<String,Bean> beans) throws BeanConfigurationException {
        Class[] size;
        Constructor cons = null;
        for(Bean bean : beans.values()){
            if(bean.getInjectionType() == 'c') // si es constructor TODO no se pueden tipos iguales
            {
                LinkedList<Class> dependencyTypes = new LinkedList<>();
                for(Property prop : bean.getProperties()) {
                    if (prop.getRef() != null) {
                        dependencyTypes.add(beans.get(prop.getRef()).getBeanClass());
                    } else {
                        dependencyTypes.add(prop.getType());
                    }
                }
                try {
                    size = new Class[dependencyTypes.size()];
                    cons = bean.getBeanClass().getDeclaredConstructor((Class[]) dependencyTypes.toArray(size));
                } catch (NoSuchMethodException e) {
                    throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
                }
                bean.setConstructor(cons);
            }
            else if(bean.isByName()) //injection byName setter
            {
                for (Property p : bean.getProperties()) {
                    for (Method method : bean.getBeanClass().getMethods()) {
                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(p.getType()) && method.getName().contains("set")) {
                            bean.addSetter(p.getType(),method);
                        }
                    }
                }
            }
            else //injection byType setter TODO no se pueden tipos iguales
            {
                Method setter = null;
                for (Property p : bean.getProperties()) {
                    //bean.addSetter(p.getType(),method);
                    String firstL = p.getName().substring(0,1).toUpperCase();
                    String setterName = "set" + firstL + p.getName().substring(1);
                    try {
                        setter = bean.getBeanClass().getDeclaredMethod(setterName,p.getType());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    bean.addSetter(p.getType(),setter);
                }
            }
        }
    }
}
