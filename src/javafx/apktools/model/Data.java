package javafx.apktools.model;

import javafx.apktools.model.config.Channel;
import javafx.apktools.model.config.Person;
import javafx.apktools.model.config.Product;
import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.resource.Resource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Data {

    private String appName;
    private String channelName;
    private String personName;
    private List<Product> product = new ArrayList<>();
    private List<Channel> channel = new ArrayList<>();
    private List<Person> person = new ArrayList<>();
    private Manifest manifest = new Manifest();
    private Resource resource = new Resource();

    public Data() {
        try {
            Document document = new SAXReader().read(new File("data.xml"));
            Element element = document.getRootElement();

            Element productElement = element.element("product");
            appName = productElement.attribute("name").getValue();
            List<Element> elements = productElement.elements();
            for (Element e : elements) {
                product.add(new Product(e.attribute("name").getValue()));
            }

            Element channelElement = element.element("channel");
            channelName = channelElement.attribute("name").getValue();
            elements = channelElement.elements();
            for (Element e : elements) {
                channel.add(new Channel(e.attribute("name").getValue(), e.attribute("mark").getValue()));
            }

            Element personElement = element.element("person");
            personName = personElement.attribute("name").getValue();
            elements = personElement.elements();
            for (Element e : elements) {
                person.add(new Person(e.attribute("name").getValue(), e.attribute("mark").getValue()));
            }

            Element manifestElement = element.element("manifest");
            elements = manifestElement.elements();
            for (Element e : elements) {
                manifest.addMetaData(e.attribute("name").getValue(), e.attribute("value").getValue());
            }

            Element resourceElement = element.element("resource");
            elements = resourceElement.elements();
            for (Element e : elements) {
                String name = e.getName();
                if ("strings".equals(name)) {
                    List<Element> items = e.elements();
                    for (Element item : items) {
                        resource.addString(item.attribute("name").getValue(), item.attribute("value").getValue());
                    }
                } else if ("bools".equals(name)) {
                    List<Element> items = e.elements();
                    for (Element item : items) {
                        resource.addBool(item.attribute("name").getValue(), item.attribute("value").getValue());
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProduct() {
        return product;
    }

    public List<Channel> getChannel() {
        return channel;
    }

    public List<Person> getPerson() {
        return person;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public Resource getResource() {
        return resource;
    }

    public String getAppName() {
        return appName;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getPersonName() {
        return personName;
    }
}
