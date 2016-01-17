package javafx.apktools.bin;

import brut.common.BrutException;
import javafx.apktools.model.manifest.Manifest;
import javafx.apktools.model.manifest.MetaData;
import javafx.apktools.model.resource.Bools;
import javafx.apktools.model.resource.Resource;
import javafx.apktools.model.resource.Strings;
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

    public Command(Callback callback) {
        this.callback = callback;
    }

    /**
     * 解包apk文件，需要aoktool.jar文件
     *
     * @param apkFilePath apk文件路径
     * @param outPath     解压路径
     */
    public boolean decodeApk(String apkFilePath, String outPath) {
//        return executeCommand("java", "-jar", "apktool.jar", "d", "-f", apkFilePath, "-o", outPath);
        try {
            brut.apktool.Main.main(new String[]{"d", "-f", apkFilePath, "-o", outPath});
            return true;
        } catch (IOException | InterruptedException | BrutException e) {
            e.printStackTrace();
            callback("解包失败 !!!!!\r\n" + e.getMessage());
        }
        return false;
    }

    /**
     * 打包apk文件，需要aoktool.jar文件
     *
     * @param buildApkFolderPath 解包apk后的文件夹路径
     * @param buildApkOutPath    打包后的文件存放路径
     */
    public boolean buildApk(String buildApkFolderPath, String buildApkOutPath) {
//        return executeCommand("java", "-jar", "apktool.jar", "b", buildApkFolderPath, "-o", buildApkOutPath);
        try {
            brut.apktool.Main.main(new String[]{"b", buildApkFolderPath, "-o", buildApkOutPath});
            return true;
        } catch (IOException | InterruptedException | BrutException e) {
            e.printStackTrace();
            callback("打包失败 !!!!!\r\n" + e.getMessage());
        }
        return false;
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
    public boolean signerApkByTime(String keystoreFilePath, String apkFilePath, String alias, String password) {
        return executeCommand("jarsigner", "-verbose", "-sigalg", "SHA1withRSA", "-tsa", "https://timestamp.geotrust.com/tsa", "-digestalg", "SHA1", "-keystore", keystoreFilePath, apkFilePath, alias, "-storepass", password);
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
    public boolean signerApk(String keystoreFilePath, String apkFilePath, String alias, String password) {
        return executeCommand("jarsigner", "-verbose", "-sigalg", "SHA1withRSA", "-digestalg", "SHA1", "-keystore", keystoreFilePath, apkFilePath, alias, "-storepass", password);
    }

    /**
     * 需要安装并Android SDK并配置环境变量Build Tools路径
     * 优化apk文件，这个需要Android Build Tools 中的zipalign程序文件
     *
     * @param apkFilePath 要优化的apk文件路径
     * @param outFilePath 优化后的apk存放文件路径
     */
    public boolean zipalign(String apkFilePath, String outFilePath) {
        return executeCommand("zipalign", "-f", "-v", "4", apkFilePath, outFilePath);
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
        if (!inputFile.exists()) {
            return;
        }
        if (inputFile.isDirectory()) {
            File[] listFile = inputFile.listFiles((pathname) -> !pathname.getName().startsWith("."));
            for (File inFile : listFile) {
                copyFile(inFile, outputFile);
            }
        } else {
            File replaceFile = new File(outputFile + File.separator + "res" + File.separator + inputFile.getParentFile().getName() + File.separator + inputFile.getName());
            File replaceFileV4 = new File(outputFile + File.separator + "res" + File.separator + inputFile.getParentFile().getName() + "-v4" + File.separator + inputFile.getName());
            if (replaceFile.exists()) {
                copyFileStream(inputFile, replaceFile);
                callback(String.format("拷贝文件 [%s] 至 ——> [%s]", inputFile.getPath(), replaceFile.getPath()));
            }
            if (replaceFileV4.exists()) {
                copyFileStream(inputFile, replaceFileV4);
                callback(String.format("拷贝文件 [%s] 至 ——> [%s]", inputFile.getPath(), replaceFileV4.getPath()));
            }
        }
    }

    /**
     * 修改AndroidManifest.xml文件
     *
     * @param appFolderName AndroidManifest.xml文件所在路径
     * @param manifest      要修改的信息
     */
    public boolean updateAndroidManifest(String appFolderName, Manifest manifest) {
        if (manifest == null) {
            return false;
        }
        try {
            File androidManifestFile = new File(appFolderName + File.separator + "AndroidManifest.xml");
            Document document = new SAXReader().read(androidManifestFile);
            Element element = document.getRootElement().element("application");
            List<Element> list = element.elements("meta-data");
            List<MetaData> metaData = manifest.getMetaData();
            boolean isUpdate = false;
            for (MetaData data : metaData) {
                String name = data.getName();
                String value = data.getValue();
                for (Element s : list) {
                    Attribute attribute = s.attribute("name");
                    if (attribute.getValue().equals(name)) {
                        s.attribute("value").setValue(value);
                        isUpdate = true;
                        callback("更新 AndroidManifest.xml meta-data name='" + attribute.getValue() + "' value='" + value + "'");
                    }
                }
            }
//            OutputFormat format = OutputFormat.createCompactFormat();
//            format.setNewlines(true);
//            format.setTrimText(true);
//            format.setIndent(true);
//            format.setIndentSize(4);
//            XMLWriter writer = new XMLWriter(new FileOutputStream(androidManifestFile), format);
            if(isUpdate){
                XMLWriter writer = new XMLWriter(new FileOutputStream(androidManifestFile));
                writer.write(document);
                writer.close();
                callback("更新 AndroidManifest.xml 完成 ~ ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 修改apktool.jar解包后生成的apktool.yml文件
     *
     * @param appFolderName apktool.yml文件所在路径
     * @param versionName   要改的版本名称
     */
    public boolean updateApkToolYmlVersion(String appFolderName, String versionName) {
        try {
            File apkToolYmlFile = new File(appFolderName + File.separator + "apktool.yml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(apkToolYmlFile)));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("versionCode")) {
                    line = "  versionCode: '" + getVersionCode(versionName) + "'";
                    callback("更新 apktool.yml " + line);
                } else if (line.contains("versionName")) {
                    line = "  versionName: '" + versionName + "'";
                    callback("更新 apktool.yml " + line);
                }
                sb.append(line).append("\r\n");
            }
            close(reader);
            FileOutputStream out = new FileOutputStream(apkToolYmlFile);
            out.write(sb.toString().getBytes());
            close(out);
            callback("更新 apktool.yml 完成 ~ ");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更改string.xml文件内容
     *
     * @param appFolderName 目标文件夹
     * @param resource      替换的resource资源
     */
    public boolean updateResource(String appFolderName, Resource resource) {
        String valuesFolderPath = appFolderName + File.separator + "res" + File.separator + "values" + File.separator;
        updateStrings(new File(valuesFolderPath + "strings.xml"), resource.getStrings());
        updateBools(new File(valuesFolderPath + "bools.xml"), resource.getBools());
        return true;
    }

    /**
     * 修改strings.xml文件内容
     *
     * @param file    strings文件
     * @param strings 修改的值列表
     */
    private void updateStrings(File file, List<Strings> strings) {
        try {
            if (strings == null || strings.isEmpty()) {
                return;
            }
            Document document = new SAXReader().read(file);
            List<Element> elements = document.getRootElement().elements();
            elements.forEach(element -> {
                final String name = element.attribute("name").getValue();
                strings.forEach(s -> {
                    if (s.getName().equals(name)) {
                        element.setText(s.getValue());
                        callback("修改 strings.xml name='" + name + "' value='" + s.getValue() + "'");
                    }
                });
            });
            XMLWriter writer = new XMLWriter(new FileOutputStream(file));
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改bools.xml文件内容
     *
     * @param file  bools文件
     * @param bools 修改的值列表
     */
    private void updateBools(File file, List<Bools> bools) {
        try {
            if (bools == null || bools.isEmpty()) {
                return;
            }
            Document document = new SAXReader().read(file);
            List<Element> elements = document.getRootElement().elements();
            elements.forEach(element -> {
                final String name = element.attribute("name").getValue();
                bools.forEach(s -> {
                    if (s.getName().equals(name)) {
                        element.setText(s.getValue());
                        callback("修改 bools.xml name='" + name + "' value='" + s.getValue() + "'");
                    }
                });
            });
            XMLWriter writer = new XMLWriter(new FileOutputStream(file));
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行命令
     *
     * @param command 命令
     */
    private synchronized boolean executeCommand(String... command) {
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
                callback(line);
                if (line.contains("Exception") || line.contains("Unable to open")) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            callback(e.getMessage());
        } finally {
            close(reader);
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 按预定返回版本号
     *
     * @param versionName 版本名称
     * @return 版本号
     */
    public String getVersionCode(String versionName) {
        char[] chars = versionName.toCharArray();
        for (char c : chars) {
            if (c == '.') {
                continue;
            }
            if (!Character.isDigit(c)) {
                throw new NumberFormatException("版本填写错误，不能包涵非数字和.点的字符");
            }
        }
        int version = 400000000;
        String[] strings = versionName.split("\\.");
        if (strings.length != 3) {
            throw new NumberFormatException("版本填写错误，请使用x.x.x的格式");
        }
        int major = Integer.parseInt(strings[0]) * 10000;
        int minor = Integer.parseInt(strings[1]) * 100;
        int build = Integer.parseInt(strings[2]);
        return String.valueOf(version + major + minor + build);
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
    public boolean copyFileStream(File inFile, File outFile) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inChannel = null, outChannel = null;
        try {
            inputStream = new FileInputStream(inFile);
            outputStream = new FileOutputStream(outFile);
            inChannel = inputStream.getChannel();
            outChannel = outputStream.getChannel();
            inChannel.transferTo(0, inFile.length(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(outChannel, inChannel, outputStream, inputStream);
        }
        return true;
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

    private void callback(String text) {
        if (callback != null) {
            callback.receiver(text);
        }
    }
}
