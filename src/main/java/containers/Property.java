package containers;

public class Property {

    private String name;
    private String ref;
    private String value;
    private Object instance;
    private Class type;

    public Property() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
            this.value = value;
        }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getRef() {
        return ref;
    }

    public Object getInstance() {
        return instance;
    }

    public Class getType() {
        return type;
    }
}
