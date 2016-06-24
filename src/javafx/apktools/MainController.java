package javafx.apktools;

import javafx.apktools.model.BuildParams;
import javafx.apktools.model.Data;
import javafx.apktools.model.config.Channel;
import javafx.apktools.model.config.Person;
import javafx.apktools.model.config.Product;
import javafx.apktools.model.resource.Strings;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller {

    public static long actionTime;

    public ComboBox<Product> product;
    public ComboBox<Channel> channel;
    public ComboBox<Person> person;
    public TextField version, buildFile, resFile;
    public ImageView image;

    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    public Stage buildStage, addChannelStage;

    private BuildParams buildInfo = new BuildParams();

    public void btnCustomAction() {
        if (!actionTime()) {
            return;
        }
        String versionText = version.getText();
        if (versionText == null || versionText.trim().length() == 0) {
            new Alert(Alert.AlertType.WARNING, "请填写版本名称", ButtonType.OK).show();
            return;
        }
        buildInfo.version = versionText;
        if (buildInfo.apkFile == null) {
            new Alert(Alert.AlertType.WARNING, "请选择apk文件", ButtonType.OK).show();
            return;
        }
        buildInfo.resource.getStrings().add(new Strings("app_name", product.getSelectionModel().getSelectedItem().name));
        if (getBuildStage() != null) {
            if (!buildStage.isShowing()) {
                buildStage.show();
            }
            getController(ApkBuildController.class).build(buildInfo);
        }
    }

    public void btnFileChooser() {
        if (!actionTime()) {
            return;
        }
        if (fileChooser == null) {
            fileChooser = new FileChooser();
        }
        ObservableList<FileChooser.ExtensionFilter> observableList = fileChooser.getExtensionFilters();
        observableList.clear();
        observableList.addAll(new FileChooser.ExtensionFilter("apk文件", "*.apk"));
        List<File> list = fileChooser.showOpenMultipleDialog(Main.stage.getOwner());
        if (list != null && list.size() > 0) {
            File file = list.get(0);
            buildInfo.apkFile = file;
            buildFile.setText(file.getName());
        }
    }

    public void btnResFileChooser() {
        if (!actionTime()) {
            return;
        }
        if (directoryChooser == null) {
            directoryChooser = new DirectoryChooser();
        }
        File directory = directoryChooser.showDialog(Main.stage.getOwner());
        if (directory == null) {
            return;
        }
        resFile.setText(directory.getPath());
        buildInfo.resFolder = directory;
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
        buildInfo.product = product.getSelectionModel().getSelectedItem();
    }

    public void personAction() {
        buildInfo.person = person.getSelectionModel().getSelectedItem();
    }

    public void channelAction() {
        buildInfo.channel.clear();
        Channel c = channel.getSelectionModel().getSelectedItem();
        if ("All".equals(c.mark)) {
            Channel[] cc = new Channel[channel.getItems().size()];
            channel.getItems().toArray(cc);
            buildInfo.channel.addAll(Arrays.asList(cc));
        } else {
            buildInfo.channel.add(c);
        }
    }

    public void addNewChannel(String name, String mark) {
        Channel c = new Channel();
        c.name = name;
        c.mark = mark;
        channel.getItems().remove(c);
        channel.getItems().add(c);
        channel.getSelectionModel().select(c);
    }

    @Override
    public void initialized(URL location, ResourceBundle resources) {
        image.setImage(new Image("/android_heander.png"));

        Data data = new Data();
        buildInfo.appName = data.getAppName();
        buildInfo.channelName = data.getChannelName();
        buildInfo.personName = data.getPersonName();

        List<Product> products = data.getProduct();
        ObservableList list = product.getItems();
        list.addAll(products);
        product.getSelectionModel().select(0);
        buildInfo.product = product.getSelectionModel().getSelectedItem();

        List<Channel> channels = data.getChannel();
        addDefChannel(channels);
        list = channel.getItems();
        list.addAll(channels);
        channel.getSelectionModel().select(0);
        buildInfo.channel.addAll(channels);

        List<Person> persons = data.getPerson();
        list = person.getItems();
        list.addAll(persons);
        person.getSelectionModel().select(0);
        buildInfo.person = person.getSelectionModel().getSelectedItem();

        buildInfo.manifest = data.getManifest();
        buildInfo.resource = data.getResource();
        version.setText("1.0.0");
        buildInfo.version = version.getText();

    }

    public void addDefChannel(List<Channel> channels) {
        Channel c = new Channel();
        c.name = "全部渠道";
        c.mark = "All";
        channels.add(0, c);
    }

    public Stage getBuildStage() {
        if (buildStage == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("fxml/apk_build.fxml"));
                buildStage = new Stage();
                buildStage.setTitle(Main.TITLE);
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
                addChannelStage.setTitle(Main.TITLE);
                addChannelStage.setScene(new Scene(root));
                addChannelStage.initModality(Modality.WINDOW_MODAL);
                addChannelStage.initStyle(StageStyle.UNIFIED);
                addChannelStage.setResizable(false);
                addChannelStage.initOwner(Main.stage);
                addChannelStage.setOnShown((event) -> getController(AddChannelController.class).initialized(null, null));
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
