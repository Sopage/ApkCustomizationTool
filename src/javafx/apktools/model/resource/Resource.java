package javafx.apktools.model.resource;

import java.util.List;

/**
 * Created by sanders on 15/8/16.
 */
public class Resource {

    private List<Strings> strings;
    private List<Bools> bools;

    public List<Strings> getStrings() {
        return strings;
    }

    public void setStrings(List<Strings> strings) {
        this.strings = strings;
    }

    public List<Bools> getBools() {
        return bools;
    }

    public void setBools(List<Bools> bools) {
        this.bools = bools;
    }
}
