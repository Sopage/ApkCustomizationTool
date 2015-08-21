package javafx.apktools.model;

/**
 * Created by sanders on 15/7/19.
 */
public class Channel {

    public String name;
    public String mark;

    @Override
    public String toString() {
        return name + "(" + mark + ")";
    }
}
