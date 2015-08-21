package javafx.apktools;

import javafx.apktools.bin.Callback;
import javafx.apktools.bin.Command;
import javafx.apktools.model.BuildConfig;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ApkBuildController extends Controller<ApkBuildController> implements Callback {

    public TextArea textArea;
    private Command command;
    private TextAreaWriteRunnable scanner = new TextAreaWriteRunnable();

    @Override
    public void initialized(URL location, ResourceBundle resources) {
        command = new Command(this);
    }

    @Override
    public void receiver(String message) {
        scanner.setText(message);
    }

    public void build(final BuildConfig params) {
        textArea.clear();
        textArea.appendText(String.format("产品：%s\r\n渠道：%s\r\n人员：%s\r\n版本：%s\r\n", params.product.name, params.channel.toString(), params.person.toString(), params.version));
        new Thread(() -> {
            File apkFile = params.apkFile;
            String buildApkFolderName = apkFile.getParent() + File.separator + apkFile.getName().replace(".apk", "").trim();
            String buildApkOutputFile = buildApkFolderName + File.separator + apkFile.getName();
            String zipalignApkOutputFile = "渠道包" + File.separator + params.channel.name + File.separator + params.product.name + "-" + params.version + ".apk";
            String replaceResourceFolder = "渠道资源" + File.separator + params.channel.name;
            scanner.setText("------------------------------" + apkFile.getName() + "开始解包......------------------------------\n");
            //用apktool解包
            command.decodeApk(apkFile.getPath(), buildApkFolderName);
            scanner.setText("------------------------------解包完成。开始定制......------------------------------\r\n");
            //替换资源文件
            command.replaceResource(replaceResourceFolder, buildApkFolderName);
            //修改AndroidManifest.xml
            command.updateAndroidManifest(buildApkFolderName, params.manifest);
            //修改apktool.yml中version的值
            command.updateApktoolYmlVersion(buildApkFolderName, params.version);
            scanner.setText("------------------------------定制完成。开始打包......------------------------------\r\n");
            //用apktool重新打包
            command.buildApk(buildApkFolderName, buildApkOutputFile);
            scanner.setText("------------------------------打包完成。开始签名......------------------------------\r\n");
            //不带时间戳的签名
            command.signerApk("android.jks", buildApkOutputFile, "android", "android");
            scanner.setText("------------------------------签名完成。开始优化......------------------------------\r\n");
            //zipalign优化
            command.zipalign(buildApkOutputFile, zipalignApkOutputFile);
            //删除反编译后的文件夹
            command.deleteFile(new File(buildApkFolderName));
        }).start();
    }

    @Override
    protected Controller getController() {
        return this;
    }


    private class TextAreaWriteRunnable implements Runnable {

        private String text;

        public synchronized void setText(String text) {
            this.text = text;
            Platform.runLater(this);
        }

        @Override
        public synchronized void run() {
            if (text == null || "".equals(text.trim())) {
                return;
            }
            textArea.appendText(text);
            text = null;
        }

    }
}
