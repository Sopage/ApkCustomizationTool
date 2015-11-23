# ApkCustomizationTool
apk定制工具，用于渠道打包等自定义apk。此工具使用JavaFX需要JDK1.8的支持。<br>
次工具适合渠道及相关人员!(不懂编程概念的相关人员)

### 功能
|功能|说明|
|----|----|
|`res`图片资源替换|丢弃之前的方式改为选择方式! 选择资源文件夹, 此资源文件夹的格式按照和开发的格式一样|
|修改`AndroidManifest.xml`中的`<mate-data>`|修改的`<mate-data>`信息请在`data.xml`文件下配置|
|修改`string.xml`和`bools.xml`|修改信息请在`data.xml`文件下配置|
|其他功能正在按需求完善||

### 配置
|文件|说明|
|----|----|
|`data.xml`|配置渠道`channel`<br>配置产品名称`product`<br>配置打包人员`person`<br>配置要修改`AndroidManifest.xml`的`meta_data`信息<br> 实现`resource`中`string.xml`和`bools.xml`的修改|
|`singer.properties`|配置签名文件位置、alias、password|

#### 编译
此项目结构在`IntelliJ IEDA`中直接运行。在运行之前请确认使用JDK8。

#### 截图
![image](https://github.com/SSOOnline/ApkCustomizationTool/raw/master/screenshot/a.png)
![image](https://github.com/SSOOnline/ApkCustomizationTool/raw/master/screenshot/b.png)
### 感谢以下项目
[Apktool](http://ibotpeaches.github.io/Apktool/)<br>
[dom4j]()