package javafx.apktools.bin;

import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.manifest.MetaData;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 定制APK命令类
 */
public class Command {

    /**
     * 回调接口
     */
    private Callback callback;

    public Command() {

    }

    public Command(Callback callback) {
        this.callback = callback;
    }

    /**
     * 解包apk文件，需要aoktool.jar文件
     *
     * @param apkFilePath apk文件路径
     * @param outPath     解压路径
     */
    public void decodeApk(String apkFilePath, String outPath) {
        executeCommand("java", "-jar", "apktool.jar", "d", "-f", apkFilePath, "-o", outPath);
    }

    /**
     * 打包apk文件，需要aoktool.jar文件
     *
     * @param buildApkFolderPath 解包apk后的文件夹路径
     * @param buildApkOutPath    打包后的文件存放路径
     */
    public void buildApk(String buildApkFolderPath, String buildApkOutPath) {
        executeCommand("java", "-jar", "apktool.jar", "b", buildApkFolderPath, "-o", buildApkOutPath);
    }

    /**
     * 需要安装JDK
     * 带时间戳的apk签名，这个需要联网。
     *
     * @param keystoreFilePath 签名文件路径
     * @param apkFilePath      被签名apk文件路径
     * @param alias            签名文件的alias名称
     * @param password         签名文件密码
     */
    public void signerApkByTime(String keystoreFilePath, String apkFilePath, String alias, String password) {
//        executeCommand("jarsigner", "-verbose", "-sigalg", "SHA1withRSA", "-tsa", "https://timestamp.geotrust.com/tsa", "-digestalg", "SHA1", "-keystore", "签名文件", apkFilePath, "签名文件alias名称", "-storepass", "签名密码");
        executeCommand("jarsigner", "-verbose", "-sigalg", "SHA1withRSA", "-tsa", "https://timestamp.geotrust.com/tsa", "-digestalg", "SHA1", "-keystore", keystoreFilePath, apkFilePath, alias, "-storepass", password);
    }

    /**
     * 需要安装JDK
     * 不带时间错的apk签名，这个不需要联网。
     *
     * @param keystoreFilePath 签名文件路径
     * @param apkFilePath      被签名apk文件路径
     * @param alias            签名文件的alias名称
     * @param password         签名文件密码
     */
    public void signerApk(String keystoreFilePath, String apkFilePath, String alias, String password) {
        executeCommand("jarsigner", "-verbose", "-sigalg", "SHA1withRSA", "-digestalg", "SHA1", "-keystore", keystoreFilePath, apkFilePath, alias, "-storepass", password);
    }

    /**
     * 需要安装并Android SDK并配置环境变量Build Tools路径
     * 优化apk文件，这个需要Android Build Tools 中的zipalign程序文件
     *
     * @param apkFilePath 要优化的apk文件路径
     * @param outFilePath 优化后的apk存放文件路径
     */
    public void zipalign(String apkFilePath, String outFilePath) {
        executeCommand("zipalign", "-f", "-v", "4", apkFilePath, outFilePath);
    }

    /**
     * 替换资源文件
     *
     * @param replaceResourceFolder 替换res资源的路径
     * @param appFolderName         被替换res资源的路径
     */
    public void replaceResource(String replaceResourceFolder, String appFolderName) {
        File infile = new File(replaceResourceFolder);
        File outFile = new File(appFolderName);
        copyFile(infile, outFile);
    }

    /**
     * 递归文件夹及文件
     *
     * @param inputFile  替换文件文件路径
     * @param outputFile 被替换文件文件路径
     */
    private void copyFile(File inputFile, File outputFile) {
        if (inputFile.isDirectory()) {
            File[] listFile = inputFile.listFiles((pathname) -> !pathname.getName().startsWith("."));
            for (File inFile : listFile) {
                File outFile = new File(outputFile.getPath() + File.separator + inFile.getName());
                copyFile(inFile, outFile);
            }
        } else {
            if (!outputFile.exists()) {
                return;
            }
            if (callback != null) {
                callback.receiver(String.format("Copy file [%s] >>>>>>>>>> [%s] \r\n", inputFile.getPath(), outputFile.getPath()));
            }
            copyFileStream(inputFile, outputFile);
        }
    }

    /**
     * 修改AndroidManifest.xml文件
     *
     * @param appFolderName AndroidManifest.xml文件所在路径
     * @param manifest      要修改的信息
     */
    public void updateAndroidManifest(String appFolderName, Manifest manifest) {
        if (manifest == null) {
            return;
        }
        try {
            File androidManifestFile = new File(appFolderName + File.separator + "AndroidManifest.xml");
            Document document = new SAXReader().read(androidManifestFile);
            Element element = document.getRootElement().element("application");
            List<Element> list = element.elements("meta-data");
            List<MetaData> metaData = manifest.getMetaData();
            for (MetaData data : metaData) {
                String name = data.getName();
                String value = data.getValue();
                for (Element s : list) {
                    Attribute attribute = s.attribute("name");
                    if (attribute.getValue().equals(name)) {
                        s.attribute("value").setValue(value);
                        if (callback != null) {
                            callback.receiver("update AndroidManifest.xml meta-data name=" + attribute.getValue() + " value=" + value + "\r\n");
                        }
                    }
                }
            }
//            OutputFormat format = OutputFormat.createCompactFormat();
//            format.setNewlines(true);
//            format.setTrimText(true);
//            format.setIndent(true);
//            format.setIndentSize(4);
//            XMLWriter writer = new XMLWriter(new FileOutputStream(androidManifestFile), format);
            XMLWriter writer = new XMLWriter(new FileOutputStream(androidManifestFile));
            writer.write(document);
            writer.close();
            if (callback != null) {
                callback.receiver("update AndroidManifest.xml OK ~ " + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改apktool.jar解包后生成的apktool.yml文件
     *
     * @param appFolderName apktool.yml文件所在路径
     * @param versionName   要改的版本名称
     */
    public void updateApktoolYmlVersion(String appFolderName, String versionName) {
        try {
            File apkToolYmlFile = new File(appFolderName + File.separator + "apktool.yml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(apkToolYmlFile)));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("versionCode")) {
                    line = "  versionCode: '" + getVersionCode(versionName) + "'";
                    if (callback != null) {
                        callback.receiver("update apktool.yml " + line + "\r\n");
                    }
                } else if (line.contains("versionName")) {
                    line = "  versionName: '" + versionName + "'";
                    if (callback != null) {
                        callback.receiver("update apktool.yml " + line + "\r\n");
                    }
                }
                sb.append(line).append("\r\n");
            }
            reader.close();
            FileOutputStream out = new FileOutputStream(apkToolYmlFile);
            out.write(sb.toString().getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行命令
     *
     * @param command 命令
     */
    private synchronized void executeCommand(String... command) {
        Process process = null;
        BufferedReader reader = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);
            builder.redirectErrorStream(true);
            process = builder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (callback != null) {
                    callback.receiver(line + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(reader);
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 按预定返回版本号
     *
     * @param versionName 版本名称
     * @return 版本号
     */
    public String getVersionCode(String versionName) {
        char[] chars = versionName.toCharArray();
        StringBuilder sb = new StringBuilder("4000");
        for (char c : chars) {
            if (c == '.') {
                c = '0';
            }
            if (!Character.isDigit(c)) {
                throw new NumberFormatException("版本填写错误，不能包涵非数字和.点的字符");
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 递归删除文件
     *
     * @param file 文件或文件夹
     */
    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * NIO文件拷贝
     *
     * @param inFile  被拷贝的文件
     * @param outFile 拷贝的文件
     */
    public void copyFileStream(File inFile, File outFile) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inChannel = null, outChannel = null;
        try {
            inputStream = new FileInputStream(inFile);
            outputStream = new FileOutputStream(outFile);
            inChannel = inputStream.getChannel();
            outChannel = outputStream.getChannel();
            inChannel.transferTo(0, inFile.length(), outChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(outChannel, inChannel, outputStream, inputStream);
        }
    }

    /**
     * 关闭资源
     *
     * @param closeable 资源
     */
    public void close(Closeable... closeable) {
        if (closeable == null) {
            return;
        }
        for (Closeable c : closeable) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
