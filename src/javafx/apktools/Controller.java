package javafx.apktools;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {

    private static final Map<Class, Object> CONTROLLER = new HashMap<Class, Object>();

    protected <T> T getController(Class<T> classT) {
        return (T) CONTROLLER.get(classT);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CONTROLLER.put(getController().getClass(), this);
        initialized(location, resources);
    }

    protected abstract void initialized(URL location, ResourceBundle resources);

    protected abstract Controller getController();

}
