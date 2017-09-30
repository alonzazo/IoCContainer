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
        String setterName, firstL;
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
            else if(!bean.isByName()) //injection byType setter
            {
                for (Property p : bean.getProperties()) {
                    for (Method method : bean.getBeanClass().getMethods()) {
                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(p.getType()) && method.getName().contains("set")) {
                            bean.addSetter(p.getName(),method);
                        }
                    }
                }
            }
            else //injection byName setter TODO no se pueden tipos iguales
            {
                Method setter = null;
                for (Property p : bean.getProperties()) {

                    if(p.getRef()!=null) {
                        firstL = p.getRef().substring(0,1).toUpperCase();
                        setterName = "set" + firstL + p.getRef().substring(1);
                    } else {
                        firstL = p.getName().substring(0,1).toUpperCase();
                        setterName = "set" + firstL + p.getName().substring(1);
                    }

                    try {
                        setter = bean.getBeanClass().getDeclaredMethod(setterName,p.getType());
                    } catch (NoSuchMethodException e) {
                        throw new BeanConfigurationException("", e); //TODO ERROR MESSAGE
                    }
                    bean.addSetter(p.getName(),setter);
                }
            }
            beans.put(bean.getName(),bean);
        }
    }
}
