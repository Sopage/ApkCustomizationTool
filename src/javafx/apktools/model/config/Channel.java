package javafx.apktools.model.config;

/**
 * Created by sanders on 15/7/19.
 */
public class Channel {

    public String name;
    public String mark;


    @Override
    public int hashCode() {
        return name.hashCode() + mark.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Channel){
            Channel c = (Channel) obj;
            if(c.name == null || c.mark == null){
                return false;
            }
            if(c.name.equals(name) && c.mark.equals(mark)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name + "(" + mark + ")";
    }
}
