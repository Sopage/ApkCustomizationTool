package javafx.apktools.model.manifest;

import java.util.ArrayList;
import java.util.List;

public class Manifest {

    private List<MetaData> metaData;

    public Manifest() {
        metaData = new ArrayList<>();
    }

    public void addMetaData(String name, String value){
        metaData.add(new MetaData(name, value));
    }

    public List<MetaData> getMetaData() {
        return metaData;
    }
}
