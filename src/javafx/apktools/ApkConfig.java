package javafx.apktools;

import com.alibaba.fastjson.JSON;
import javafx.apktools.model.*;
import javafx.apktools.model.config.Channel;
import javafx.apktools.model.config.Person;
import javafx.apktools.model.config.Product;
import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.resource.Resource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ApkConfig {

    private static final String CONFIG_FILE_NAME = "config.json";
    private static ApkConfig config;
    private ConfigInfo configInfo;


    public static synchronized ApkConfig getConfig() {
        if (config == null) {
            config = new ApkConfig();
        }
        return config;
    }

    private ApkConfig() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CONFIG_FILE_NAME)));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            configInfo = JSON.parseObject(sb.toString(), ConfigInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Product> getProductList() {
        return configInfo.getProduct();
    }

    public List<Person> getPersonList() {
        return configInfo.getPerson();
    }

    public List<Channel> getChannelList() {
        return configInfo.getChannel();
    }

    public Manifest getManifest(){
        return configInfo.getManifest();
    }

    public Resource getResource(){
        return configInfo.getResource();
    }

}
