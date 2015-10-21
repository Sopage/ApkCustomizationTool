package javafx.apktools.model.resource;

/**
 * Created by sanders on 15/10/21.
 */
public class Values {

    private String name;
    private String value;

    public Values() {
    }

    public Values(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
