# ApkCustomizationTool
apk定制工具，用于渠道打包等自定义apk。此工具使用JavaFX开发需要JDK1.8的支持。<br>
此工具适合渠道及相关人员!(不懂编程概念的相关人员)<br>
注意:请在环境变量里面配置好`jarsigner`签名工具和`zipalign`优化工具

### 功能
|功能|说明|
|----|----|
|`res`图片资源替换|选择资源文件夹, 资源文件夹的格式和开发的格式一样。替换的文件及格式也要一样。选中res文件夹即可![image](https://github.com/SSOOnline/ApkCustomizationTool/raw/master/screenshot/0.png)|
|修改`AndroidManifest.xml`中的`<mate-data>`|修改的`<mate-data>`信息请在`data.xml`文件下配置|
|修改`string.xml`和`bools.xml`|修改信息请在`data.xml`文件下配置|
|其他功能正在按需求完善||

### 配置
|文件|说明|
|----|----|
|`data.xml`|配置渠道`channel`<br>配置产品名称`product`<br>配置打包人员`person`<br>配置要修改`AndroidManifest.xml`的`meta_data`信息<br> 实现`resource`中`string.xml`和`bools.xml`的修改<br>详情请查阅`data.xml`中的注释|
|`singer.properties`|配置签名文件位置、alias、password|

### 说明
版本号开始数为`100000000`,前三位是大版本号,中间三位为中版本号,后面三位为小版本号,版本填写规则为`x.x.x`如·6.0.0·,每一位不能大于100并且是纯数字。<br>
如果要修改自己的版本升级策略求修改源码`Command.java`中的`getVersionCode`方法。

#### 编译
此项目结构在`IntelliJ IEDA`中直接运行。在运行之前请确认使用JDK8。

#### 截图
![image](https://github.com/SSOOnline/ApkCustomizationTool/raw/master/screenshot/1.png)
![image](https://github.com/SSOOnline/ApkCustomizationTool/raw/master/screenshot/2.png)
### 感谢以下项目
[Apktool](http://ibotpeaches.github.io/Apktool/)<br>
[dom4j]()