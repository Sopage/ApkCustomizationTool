package javafx.apktools.model.manifest;

import javafx.apktools.model.resource.NV;

/**
 * AndroidManifest.xml meta-data标签包装
 */
public class MetaData extends NV {

    public MetaData() {
        super();
    }

    public MetaData(String name, String value) {
        super(name, value);
    }
}
