package javafx.apktools.model;

import javafx.apktools.model.config.Channel;
import javafx.apktools.model.config.Person;
import javafx.apktools.model.config.Product;
import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.resource.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanders on 15/7/19.
 */
public class BuildInfo {

    public File apkFile;
    public Product product;
    public List<Channel> channel = new ArrayList<>();
    public Person person;
    public Manifest manifest;
    public String version;
    public Resource resource;

}
