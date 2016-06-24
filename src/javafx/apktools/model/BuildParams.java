package javafx.apktools.model;

import javafx.apktools.model.config.Channel;
import javafx.apktools.model.config.Person;
import javafx.apktools.model.config.Product;
import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.resource.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BuildParams {

    public String keyStoreFilePath;
    public String keyStoreAlias;
    public String keyStorePassword;
    public boolean signerTSA;

    public BuildParams() {
        try {
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream("signer.properties");
            properties.load(inputStream);
            keyStoreFilePath = properties.getProperty("KEY_STORE_FILE_PATH", "");
            keyStoreAlias = properties.getProperty("KEY_STORE_ALIAS", "");
            keyStorePassword = properties.getProperty("KEY_STORE_PASSWORD", "");
            signerTSA = Boolean.parseBoolean(properties.getProperty("SIGNER_TSA", "false"));
            inputStream.close();
            properties.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File apkFile;
    public File resFolder;
    public Product product;
    public List<Channel> channel = new ArrayList<>();
    public String appName;
    public String channelName;
    public Person person;
    public String personName;
    public Manifest manifest;
    public String version;
    public Resource resource;
}
