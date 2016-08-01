package javafx.apktools.model.config;

public class Channel {

    public String name;
    public String mark;

    public Channel() {
    }

    public Channel(String name, String mark) {
        this.name = name;
        this.mark = mark;
    }

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
