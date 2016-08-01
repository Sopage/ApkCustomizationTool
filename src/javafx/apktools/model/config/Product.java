package javafx.apktools.model.config;

public class Product {

    public String name;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
