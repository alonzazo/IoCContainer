package containers;

public class Property {

    private String name;
    private Object instance;
    private String ref;
    private String value;


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

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getRef() {
        return ref;
    }

    public String toString() {
        String str = "  Property name: "+name;
        if(value != null) {
            str += "\n  value: "+value;
        } else if (ref != null) {
            str += "\n  ref: "+ref;
        }
        return str;
    }
}
