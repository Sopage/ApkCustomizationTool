# ApkCustomizationTool
apk定制工具，用于渠道打包等自定义apk。此工具使用JavaFX需要Java8以上的支持

### 功能
|功能|
|----|
|res图片资源替换|
|修改AndroidManifest.xml中的`<mate-data>`|
|其他功能正在按需求完善|

### 配置
|文件|说明|
|----|----|
|config.json|配置渠道`channel`<br>配置产品名称`product`<br>配置打包人员`person`<br>配置要修改`AndroidManifest.xml`的`meta_data`信息<br>暂未实现`resource`|
|config.properties|配置签名文件位置、alias、password|

### 感谢以下项目
[Apktool](http://ibotpeaches.github.io/Apktool/)<br>
[fastjson](https://github.com/alibaba/fastjson)<br>
[dom4j]()
