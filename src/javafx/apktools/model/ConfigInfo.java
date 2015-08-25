package javafx.apktools.model;

import javafx.apktools.model.config.Channel;
import javafx.apktools.model.config.Person;
import javafx.apktools.model.config.Product;
import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.resource.Resource;

import java.util.List;

/**
 * 配置映射类
 */
public class ConfigInfo {

    private List<Product> product;
    private List<Channel> channel;
    private List<Person> person;
    private Manifest manifest;
    private Resource resource;

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public List<Channel> getChannel() {
        return channel;
    }

    public void setChannel(List<Channel> channel) {
        this.channel = channel;
    }

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
