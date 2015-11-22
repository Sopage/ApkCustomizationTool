package javafx.apktools;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddChannelController extends Controller {

    public TextField channelName, channelMark;

    public void btnCancel() {
        MainController controller = getController(MainController.class);
        if (controller.addChannelStage != null && controller.addChannelStage.isShowing()) {
            controller.addChannelStage.close();
        }
    }

    public void btnConfirm() {
        String name = channelName.getText();
        String mark = channelMark.getText();
        if (name == null || name.trim().length() < 1) {
            new Alert(Alert.AlertType.WARNING, "请填写渠道名称", ButtonType.OK).show();
            return;
        }
        if (mark == null || mark.trim().length() < 1) {
            new Alert(Alert.AlertType.WARNING, "请填写渠道标识", ButtonType.OK).show();
            return;
        }
        MainController controller = getController(MainController.class);
        if (controller.addChannelStage != null && controller.addChannelStage.isShowing()) {
            controller.addChannelStage.close();
            controller.addNewChannel(name.trim(), mark.trim());
        }
    }

    @Override
    protected void initialized(URL location, ResourceBundle resources) {
        channelName.setText("");
        channelMark.setText("");
    }

    @Override
    protected Controller getController() {
        return this;
    }
}
