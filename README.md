# ApkCustomizationTool
apk定制工具，用于渠道打包等自定义apk。此工具使用JavaFX需要JDK1.8及以上的支持。

### 功能
|功能|说明|
|----|----|
|`res`图片资源替换|要替换的资源文件放在 渠道资源文件夹中对应的文件夹下，并且命名和图片资源格式要和被替换资源一致|
|修改`AndroidManifest.xml`中的`<mate-data>`|修改的`<mate-data>`信息请在`config.json`文件下配置|
|其他功能正在按需求完善|

### 配置
|文件|说明|
|----|----|
|`config.json`|配置渠道`channel`<br>配置产品名称`product`<br>配置打包人员`person`<br>配置要修改`AndroidManifest.xml`的`meta_data`信息<br>暂未实现`resource`|
|`config.properties`|配置签名文件位置、alias、password|

#### 编译
此项目结构在`IntelliJ IEDA`IDE中直接运行。在运行之前请确认使用JDK8的新语法特性。

### 感谢以下项目
[Apktool](http://ibotpeaches.github.io/Apktool/)<br>
[fastjson](https://github.com/alibaba/fastjson)<br>
[dom4j]()