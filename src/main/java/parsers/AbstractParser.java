package parsers;

import containers.AbstractBeanFactory;
import containers.Bean;
import containers.Property;
import exceptions.BeanConfigurationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractParser implements Parser{

    public void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException {}
    public void getInjectors(HashMap<String,Bean> beans) throws BeanConfigurationException {
        Class[] size;
        Constructor cons = null;
        String setterName, firstL;
        int setterCount;
        LinkedList<Class> dependencyTypes;
        for(Bean bean : beans.values()){
            if(bean.getInjectionType() == 'c') // si es constructor
            {
                dependencyTypes = new LinkedList<>();
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
                    throw new BeanConfigurationException("No suitable constructor for bean \""+bean.getName()+"\" of class \""+bean.getBeanClass().getName()+"\".", e);
                }
                bean.setConstructor(cons);
            }
            else if(!bean.isByName()) //injection byType setter
            {
                for (Property p : bean.getProperties()) {
                    setterCount = 0;
                    for (Method method : bean.getBeanClass().getMethods()) {
                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(p.getType()) && method.getName().contains("set")) {
                            bean.addSetter(method);
                            setterCount++;
                        }
                    }
                    if(setterCount < 1){
                        throw new BeanConfigurationException("No suitable setter method found for property \""+p.getName()+"\" of bean \""+bean.getName()+"\" of class \""+bean.getBeanClass().getName()+"\".");
                    } else if (setterCount > 1) {
                        throw new BeanConfigurationException("More than one suitable setter method found for property \""+p.getName()+"\" of bean \""+bean.getName()+"\" of class \""+bean.getBeanClass().getName()+"\".");
                    }
                }
            }
            else //injection byName setter
            {
                Method setter = null;
                for (Property p : bean.getProperties()) {
                    firstL = p.getName().substring(0,1).toUpperCase();
                    setterName = "set" + firstL + p.getName().substring(1);
                    try {
                        setter = bean.getBeanClass().getDeclaredMethod(setterName,p.getType());
                        if(setter.getParameterCount() != 1) {
                            throw new BeanConfigurationException("Setter method \""+setter.getName()+"\" for property \""+p.getName()+"\" of bean \""+bean.getName()+"\" in class \""+bean.getBeanClass().getName()+"\" must have exactly one parameter.");
                        }
                    } catch (NoSuchMethodException e) {
                        throw new BeanConfigurationException("No suitable setter method found for property \""+p.getName()+"\" of bean \""+bean.getName()+"\" of class \""+bean.getBeanClass().getName()+"\".", e);
                    }
                    bean.addSetter(setter);
                }
            }
            beans.put(bean.getName(),bean);
        }
    }
}
