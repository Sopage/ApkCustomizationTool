package javafx.apktools.model.resource;

import java.util.ArrayList;
import java.util.List;

public class Resource {

    private List<Strings> strings;
    private List<Bools> bools;

    public Resource(){
        strings = new ArrayList<>();
        bools = new ArrayList<>();
    }

    public void addString(String name, String value){
        strings.add(new Strings(name, value));
    }

    public void addBool(String name, String value){
        bools.add(new Bools(name, value));
    }

    public List<Strings> getStrings() {
        return strings;
    }

    public List<Bools> getBools() {
        return bools;
    }

}
