package javafx.apktools;

import javafx.apktools.model.BuildParams;
import javafx.apktools.model.config.Channel;
import javafx.apktools.model.config.Person;
import javafx.apktools.model.config.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller<MainController> {

    public static long actionTime;

    public ComboBox<Product> product;
    public ComboBox<Channel> channel;
    public ComboBox<Person> person;
    public TextField version, buildFile;
    public ImageView image;

    private FileChooser fileChooser;
    public Stage buildStage, addChannelStage;

    private BuildParams info = new BuildParams();

    public void btnCustomAction() {
        if (!actionTime()) {
            return;
        }
        String versionText = version.getText();
        if (versionText == null || versionText.trim().length() == 0) {
            new Alert(Alert.AlertType.WARNING, "请填写版本名称", ButtonType.OK).show();
            return;
        }
        info.version = versionText;
        if (info.apkFile == null) {
            new Alert(Alert.AlertType.WARNING, "请选择apk文件", ButtonType.OK).show();
            return;
        }
        if (getBuildStage() != null) {
            if (!buildStage.isShowing()) {
                buildStage.show();
            }
            getController(ApkBuildController.class).build(info);
        }
    }

    public void btnFileChooser() {
        if (!actionTime()) {
            return;
        }
        if (fileChooser == null) {
            fileChooser = new FileChooser();
        }
        List<File> list = new FileChooser().showOpenMultipleDialog(Main.stage.getOwner());
        if (list != null && list.size() > 0) {
            File file = list.get(0);
            info.apkFile = file;
            buildFile.setText(file.getName());
        }
    }

    public void btnAddChannel() {
        if (!actionTime()) {
            return;
        }
        if (getAddChannelStage() != null) {
            getAddChannelStage().show();
        }
    }

    public void productAction() {
        info.product = product.getSelectionModel().getSelectedItem();
    }

    public void personAction() {
        info.person = person.getSelectionModel().getSelectedItem();
    }

    public void channelAction() {
        info.channel.clear();
        Channel c = channel.getSelectionModel().getSelectedItem();
        if ("ALL".equals(c.mark)) {
            Channel[] cc = new Channel[channel.getItems().size()];
            channel.getItems().toArray(cc);
            info.channel.addAll(Arrays.asList(cc));
        } else {
            info.channel.add(c);
        }
    }

    public void addNewChannel(String name, String mark) {
        Channel c = new Channel();
        c.name = name;
        c.mark = mark;
        channel.getItems().remove(c);
        channel.getItems().add(c);
        channel.getSelectionModel().select(c);
        File file = new File("渠道包" + File.separator + name);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void initialized(URL location, ResourceBundle resources) {
        image.setImage(new Image("/ic_launcher.png"));

        List<Product> products = ApkConfig.getConfig().getConfigInfo().getProduct();
        ObservableList list = product.getItems();
        list.addAll(products);
        product.getSelectionModel().select(0);
        info.product = product.getSelectionModel().getSelectedItem();

        List<Channel> channels = ApkConfig.getConfig().getConfigInfo().getChannel();
        addDefChannel(channels);
        list = channel.getItems();
        list.addAll(channels);
        channel.getSelectionModel().select(0);
        info.channel.addAll(channels);

        List<Person> persons = ApkConfig.getConfig().getConfigInfo().getPerson();
        list = person.getItems();
        list.addAll(persons);
        person.getSelectionModel().select(0);
        info.person = person.getSelectionModel().getSelectedItem();

        info.manifest = ApkConfig.getConfig().getConfigInfo().getManifest();
        info.resource = ApkConfig.getConfig().getConfigInfo().getResource();
        version.setText("6.1.1");
        info.version = version.getText();

    }

    public void addDefChannel(List<Channel> channels) {
        Channel c = new Channel();
        c.name = "全部渠道";
        c.mark = "ALL";
        channels.add(0, c);
    }

    public Stage getBuildStage() {
        if (buildStage == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("fxml/apk_build.fxml"));
                buildStage = new Stage();
                buildStage.setTitle("APK定制工具(YoYo~专版)");
                buildStage.setScene(new Scene(root));
                buildStage.initModality(Modality.NONE);
                buildStage.initStyle(StageStyle.UNIFIED);
                buildStage.setResizable(false);
                buildStage.initOwner(Main.stage);
                buildStage.setX(Main.stage.getX());
                buildStage.setY(Main.stage.getY() + Main.stage.getHeight());
                buildStage.setOnShown((event) -> {

                });
            } catch (Exception e) {
                e.printStackTrace();
                buildStage = null;
            }
        }
        return buildStage;
    }


    public Stage getAddChannelStage() {
        if (addChannelStage == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("fxml/add_channel.fxml"));
                addChannelStage = new Stage();
                addChannelStage.setTitle("APK定制工具(YoYo~专版)");
                addChannelStage.setScene(new Scene(root));
                addChannelStage.initModality(Modality.WINDOW_MODAL);
                addChannelStage.initStyle(StageStyle.UNIFIED);
                addChannelStage.setResizable(false);
                addChannelStage.initOwner(Main.stage);
                addChannelStage.setOnShown((event) -> {
                    getController(AddChannelController.class).initialized(null, null);
                });
            } catch (Exception e) {
                e.printStackTrace();
                addChannelStage = null;
            }
        }
        return addChannelStage;
    }

    public boolean actionTime() {
        if (System.currentTimeMillis() - actionTime > 600) {
            actionTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    protected Controller getController() {
        return this;
    }
}
