package javafx.apktools;

import javafx.apktools.bin.Callback;
import javafx.apktools.bin.Command;
import javafx.apktools.model.BuildParams;
import javafx.apktools.model.config.Channel;
import javafx.apktools.model.manifest.MetaData;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ApkBuildController extends Controller implements Callback {

    public TextArea textArea;
    private Command command;
    private boolean build = false;

    @Override
    public void initialized(URL location, ResourceBundle resources) {
        command = new Command(this);
    }

    @Override
    public void receiver(String message) {
        setText(message);
    }

    public void build(BuildParams params) {
        if (build) {
            new Alert(Alert.AlertType.WARNING, "正在打包中,请稍后...", ButtonType.OK).show();
            return;
        }
        build = true;
        textArea.clear();
        new Thread(() -> {
            File apkFile = params.apkFile;
            String buildApkFolderName = apkFile.getParent() + File.separator + apkFile.getName().replace(".apk", "").trim();
            String buildApkOutputFile = buildApkFolderName + File.separator + apkFile.getName();
            List<Channel> channels = params.channel;
            for (Channel channel : channels) {
                if ("All".equals(channel.mark)) {
                    continue;
                }
                params.manifest.getMetaData().clear();
                params.manifest.getMetaData().add(new MetaData(params.personName, params.person.mark));
                params.manifest.getMetaData().add(new MetaData(params.channelName, channel.mark));
                setText(String.format("\r\n产品：%s\r\n渠道：%s\r\n人员：%s\r\n版本：%s", params.product.name, channel.toString(), params.person.toString(), params.version));
                String zipalignApkOutputFile = "渠道包" + File.separator + channel.name + "-" + params.product.name + "-" + params.version + ".apk";
                setText("1. --> 开始解包" + apkFile.getName());
                //用apktool解包
                if (!command.decodeApk(apkFile.getPath(), buildApkFolderName)) {
                    setText("打包终止!!!!!!");
                    return;
                }
                setText("2. --> 开始定制");
                //替换资源文件
                if (params.resFolder != null) {
                    command.replaceResource(params.resFolder.getPath(), buildApkFolderName);
                }
                //修改AndroidManifest.xml
                if (!command.updateAndroidManifest(buildApkFolderName, params.manifest)) {
                    setText("打包终止!!!!!!");
                    return;
                }
                //修改apktool.yml中version的值
                if (!command.updateApkToolYmlVersion(buildApkFolderName, params.version)) {
                    setText("打包终止!!!!!!");
                    return;
                }
                //修改res/values文件夹下面的资源
                command.updateResource(buildApkFolderName, params.resource);
                setText("3. --> 开始打包");
                //用apktool重新打包
                if (!command.buildApk(buildApkFolderName, buildApkOutputFile)) {
                    setText("打包终止!!!!!!");
                    return;
                }
                setText("4. --> 开始签名");
                if (params.signerTSA) {
                    //带时间戳的签名
                    if(!command.signerApkByTime(params.keyStoreFilePath, buildApkOutputFile, params.keyStoreAlias, params.keyStorePassword)){
                        setText("打包终止!!!!!!");
                        return;
                    }
                } else {
                    //不带时间戳的签名
                    if(!command.signerApk(params.keyStoreFilePath, buildApkOutputFile, params.keyStoreAlias, params.keyStorePassword)){
                        setText("打包终止!!!!!!");
                        return;
                    }
                }
                setText("5. --> 开始优化");
                //zipalign优化
                new File(zipalignApkOutputFile).getParentFile().mkdirs();
                if(!command.zipalign(buildApkOutputFile, zipalignApkOutputFile)){
                    setText("打包终止!!!!!!");
                    return;
                }
                //删除反编译后的文件夹
                command.deleteFile(new File(buildApkFolderName));
            }
            setText("———————————————————————————————————————————————————");
            setText("|    定制完成，你可以测试每个定制后的包是否定制正确！    |");
            setText("———————————————————————————————————————————————————");
            build = false;
        }).start();
    }

    public void setText(String text) {
        Platform.runLater(() -> textArea.appendText(text + "\r\n"));
    }

    @Override
    protected Controller getController() {
        return this;
    }
}
