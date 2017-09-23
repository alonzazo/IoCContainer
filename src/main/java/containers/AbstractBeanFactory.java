package containers;

import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactory {

    protected HashMap<String, Bean> beans;

    public AbstractBeanFactory()
    {
        beans = new HashMap<>();
    }

    public void addBean(String name, Bean bean) {
        beans.put(name, bean);
    }


}
