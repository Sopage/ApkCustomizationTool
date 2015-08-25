package javafx.apktools;

import javafx.apktools.model.config.Channel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.List;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        primaryStage.setTitle("APK定制工具(YoYo~专版)");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
    }

    @Override
    public void stop() throws Exception {
        stage = null;
        super.stop();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            File file;
            String[] folders = {"drawable", "drawable-hdpi", "drawable-mdpi", "drawable-xhdpi", "drawable-xxhdpi"};
            List<Channel> channels = ApkConfig.getConfig().getChannelList();
            String root = "渠道资源";
            String apk = "渠道包";
            for (Channel channel : channels) {
                file = new File(apk + File.separator + channel.name);
                file.mkdirs();
                for (String folder : folders) {
                    file = new File(root + File.separator + channel.name + File.separator + "res" + File.separator + folder);
                    file.mkdirs();
                }
            }
        }).start();
        launch(args);
    }

}
