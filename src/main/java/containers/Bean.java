package containers;

import java.util.LinkedList;

public class Bean {

    LinkedList<Property> properties;
    String name;
    Object singletonInstance;
    Class beanClass;
    String PostConstruct;
    String PreDestruct;
    String targetSetter;

    public Bean()
    {

    }

    public boolean isSingletoon()
    {
        return true;
    }
}
