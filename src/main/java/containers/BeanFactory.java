package containers;

import java.util.HashMap;

public abstract class BeanFactory {
    private HashMap<String, Bean> beans;

    public void addBean(String name, Bean bean) {
        beans.put(name, bean);
    }

}
