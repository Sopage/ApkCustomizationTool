package javafx.apktools;

import javafx.apktools.bin.Callback;
import javafx.apktools.bin.Command;
import javafx.apktools.model.BuildInfo;
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

public class ApkBuildController extends Controller<ApkBuildController> implements Callback {

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

    public void build(final BuildInfo params) {
        if(build){
            new Alert(Alert.AlertType.WARNING, "打包中... 完成后请再次操作", ButtonType.OK).show();
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
                if ("ALL".equals(channel.mark)) {
                    continue;
                }
                params.manifest.getMetaData().clear();
                params.manifest.getMetaData().add(new MetaData("renyuan", params.person.mark));
                params.manifest.getMetaData().add(new MetaData("qudao", channel.mark));
                setText(String.format("\r\n产品：%s\r\n渠道：%s\r\n人员：%s\r\n版本：%s\r\n", params.product.name, channel.toString(), params.person.toString(), params.version));
                String zipalignApkOutputFile = "渠道包" + File.separator + channel.name + File.separator + params.product.name + "-" + params.version + ".apk";
                String replaceResourceFolder = "渠道资源" + File.separator + channel.name;
                setText("---------------------------  开始解包" + apkFile.getName() + "  ---------------------------\r\n");
                //用apktool解包
                command.decodeApk(apkFile.getPath(), buildApkFolderName);
                setText("---------------------------  解包完成，开始定制  ---------------------------\r\n");
                //替换资源文件
                command.replaceResource(replaceResourceFolder, buildApkFolderName);
                //修改AndroidManifest.xml
                command.updateAndroidManifest(buildApkFolderName, params.manifest);
                //修改apktool.yml中version的值
                command.updateApkToolYmlVersion(buildApkFolderName, params.version);
                setText("---------------------------  定制完成，开始打包  ---------------------------\r\n");
                //用apktool重新打包
                command.buildApk(buildApkFolderName, buildApkOutputFile);
                setText("---------------------------  打包完成，开始签名  ---------------------------\r\n");
                //不带时间戳的签名
                command.signerApk("handsgo.keystore", buildApkOutputFile, "handsgo.keystore", "978%^6b82c7d5");
                setText("---------------------------  签名完成，开始优化  ---------------------------\r\n");
                //zipalign优化
                command.zipalign(buildApkOutputFile, zipalignApkOutputFile);
                //删除反编译后的文件夹
                command.deleteFile(new File(buildApkFolderName));
            }
            setText("———————————————————————————————————————————————————\r\n");
            setText("|    定制完成，你可以测试每个定制后的包是否定制正确！    |\r\n");
            setText("———————————————————————————————————————————————————");
            build = false;
        }).start();
    }

    public void setText(String text) {
        Platform.runLater(() -> textArea.appendText(text));
    }

    @Override
    protected Controller getController() {
        return this;
    }
}
