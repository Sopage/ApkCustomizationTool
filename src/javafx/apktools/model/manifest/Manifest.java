package javafx.apktools.model.manifest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanders on 15/8/16.
 */
public class Manifest {

    private List<MetaData> meta_data;

    public List<MetaData> getMetaData() {
        if (meta_data == null) {
            meta_data = new ArrayList<>();
        }
        return meta_data;
    }

    public void setMeta_data(List<MetaData> meta_data) {
        this.meta_data = meta_data;
    }
}
