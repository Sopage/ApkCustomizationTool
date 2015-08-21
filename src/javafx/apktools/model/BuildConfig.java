package javafx.apktools.model;

import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.resource.Resource;

import java.io.File;

/**
 * Created by sanders on 15/7/19.
 */
public class BuildConfig {

    public File apkFile;
    public Product product;
    public Channel channel;
    public Person person;
    public Manifest manifest;
    public String version;
    public Resource resource;

}
