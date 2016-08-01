package javafx.apktools.model.config;

public class Person {

    public String name;
    public String mark;

    public Person() {
    }

    public Person(String name, String mark) {
        this.name = name;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return name + "(" + mark + ")";
    }
}
