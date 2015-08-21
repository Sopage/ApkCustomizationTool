package javafx.apktools;

import javafx.apktools.model.BuildConfig;
import javafx.apktools.model.Channel;
import javafx.apktools.model.Person;
import javafx.apktools.model.Product;
import javafx.apktools.model.manifest.MetaData;
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
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller<MainController> {

    public ComboBox<Product> product;
    public ComboBox<Channel> channel;
    public ComboBox<Person> person;
    public TextField version, buildFile;
    public ImageView image;

    private FileChooser fileChooser;
    private Stage buildStage;

    private BuildConfig info = new BuildConfig();

    public void btnCustomAction() {
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
            info.manifest.getMetaData().add(new MetaData("qudao", info.channel.mark));
            info.manifest.getMetaData().add(new MetaData("renyuan", info.person.mark));
            buildStage.show();
            getController(ApkBuildController.class).build(info);
        }
    }

    public void btnFileChooser() {
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

    public void productAction() {
        info.product = product.getSelectionModel().getSelectedItem();
    }

    public void personAction() {
        info.person = person.getSelectionModel().getSelectedItem();
    }

    public void channelAction() {
        info.channel = channel.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialized(URL location, ResourceBundle resources) {
        image.setImage(new Image("/ic_launcher.png"));

        List<Product> products = ApkConfig.getConfig().getProductList();
        ObservableList list = product.getItems();
        list.addAll(products);
        product.getSelectionModel().select(0);
        info.product = product.getSelectionModel().getSelectedItem();
        version.setText(info.product.beforeVersion);

        List<Channel> channels = ApkConfig.getConfig().getChannelList();
        list = channel.getItems();
        list.addAll(channels);
        channel.getSelectionModel().select(0);
        info.channel = channel.getSelectionModel().getSelectedItem();

        List<Person> persons = ApkConfig.getConfig().getPersonList();
        list = person.getItems();
        list.addAll(persons);
        person.getSelectionModel().select(0);
        info.person = person.getSelectionModel().getSelectedItem();

        info.manifest = ApkConfig.getConfig().getManifest();
        info.resource = ApkConfig.getConfig().getResource();
        version.setText("6.1.0");
        info.version = version.getText();

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

    @Override
    protected Controller getController() {
        return this;
    }
}
