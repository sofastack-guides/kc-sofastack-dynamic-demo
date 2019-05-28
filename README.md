## SOFABoot 动态模块实践

### 实验内容

本实验基于 SOFADashboard 完成 SOFABoot 动态模块实践，主要包括以下几个部分：

- 将 SOFABoot 应用打包成 ark 包
- 将 SOFABoot 应用改造成动态模块的宿主应用
  - 动态模块依赖
  - 动态模块配置
- SOFADashboard 命令推送
  - 安装
  - 激活
  - 卸载

### 架构图

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*WQJ1Rpw_w3UAAAAAAAAAAABkARQnAQ)

### 任务

#### 1、任务准备

从 github 上将 demo 工程克隆到本地

```bash
git clone https://github.com/sofastack-guides/kc-sofastack-dynamic-demo.git
```

然后将工程导入到 IDEA 或者 eclipse。导入之后界面如下：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*X8peTJg-RDkAAAAAAAAAAABkARQnAQ)

#### 2、将 SOFABoot 应用打包成 ark 包

在下图所示的工程 pom 配置中，增加 ark 打包插件，并进行配置：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*Yo5ZTZ14Kr0AAAAAAAAAAABkARQnAQ)

- step1 : 将 ark 打包插件及配置粘贴在上图指定位置中

```xml
<plugin>
  <groupId>com.alipay.sofa</groupId>
  <artifactId>sofa-ark-maven-plugin</artifactId>
  <version>0.6.0</version>
  <executions>
    <execution>
      <!--goal executed to generate executable-ark-jar -->
      <goals>
        <goal>repackage</goal>
      </goals>
      <!--ark-biz 包的打包配置  -->
      <configuration>
        <!--是否打包、安装和发布 ark biz，详细参考 Ark Biz 文档，默认为false-->
        <attach>true</attach>
        <!--ark 包和 ark biz 的打包存放目录，默认为工程 build 目录-->
        <outputDirectory>target</outputDirectory>
        <!--default none-->
        <arkClassifier>executable-ark</arkClassifier>
        <!-- ark-biz 包的启动优先级，值越小，优先级越高-->
        <priority>200</priority>
        <!--设置应用的根目录，用于读取 ${base.dir}/conf/ark/bootstrap.application 配置文件，默认为 ${project.basedir}-->
        <baseDir>../</baseDir>
      </configuration>
    </execution>
  </executions>
</plugin>
```

- step2 : 配置完成之后，执行 mvn clean package 进行 打包，成功之后如下图所示

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*IcHRRJt8luIAAAAAAAAAAABkARQnAQ)

#### 3、构建宿主应用

在已下载下来的工程中，kc-sofastack-ark-biz-master 作为实验的宿主应用工程模型。通过此任务，将 kc-sofastack-ark-biz-master 构建成为 动态模块的宿主应用。

- step1 : 引入 ark 动态配置依赖

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*q1ECS5zJXxMAAAAAAAAAAABkARQnAQ)

1、SOFAArk 相关依赖

```xml
<dependency>
  <groupId>com.alipay.sofa</groupId>
  <artifactId>sofa-ark-springboot-starter</artifactId>
</dependency>
<dependency>
  <groupId>com.alipay.sofa</groupId>
  <artifactId>web-ark-plugin</artifactId>
</dependency>
<dependency>
  <groupId>com.alipay.sofa</groupId>
  <artifactId>config-ark-plugin</artifactId>
</dependency>
```

2、provider 依赖

```xml
<dependency>
  <groupId>io.sofastack</groupId>
  <artifactId>kc-sofastack-ark-biz-provider</artifactId>
  <version>1.0.0</version>
  <classifier>ark-biz</classifier>
</dependency>
```

- step2 : 动态模块配置

在工程根目录结构下的 /conf/ark/bootstrap.properties 配置文件中添加如下配置

```properties
# 日志根目录
logging.path=./logs
# 配置服务器地址
com.alipay.sofa.ark.config.address=zookeeper://zookeeper-1-dev.sofastack.tech:2181
# 宿主应用名
com.alipay.sofa.ark.master.biz=ark-biz-master
```

- step3 : 配置 ark 打包插件

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*4nKgQ7I3RrMAAAAAAAAAAABkARQnAQ)

将如下插件打包配置粘贴至上图指定位置

```xml
<plugin>
	<groupId>com.alipay.sofa</groupId>
  <artifactId>sofa-ark-maven-plugin</artifactId>
  <version>0.6.0</version>
  <executions>
    <execution>
      <id>default-cli</id>
      <goals>
        <goal>repackage</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <priority>100</priority>
    <baseDir>../</baseDir>
    <bizName>ark-biz-master</bizName>
  </configuration>
</plugin>
```

- step4 ： 配置宿主应用 application.properties 

```xml
#dashboard client config
management.endpoints.web.exposure.include=*
com.alipay.sofa.dashboard.zookeeper.address=zookeeper-1-dev.sofastack.tech:2181

#skip jvm health check to startup host-app
com.alipay.sofa.boot.skip-jvm-reference-health-check=true
```

将是上面的配置项服务到下图指定位置：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*MPy_TbqvXegAAAAAAAAAAABkARQnAQ)

#### 4、打包宿主应用 & 启动

- step 1 ： mvn clean package 打包

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*AM1fSYK4xaIAAAAAAAAAAABkARQnAQ)

打包成功如下图所示:

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*P5BoQ44H5aIAAAAAAAAAAABkARQnAQ)

- step2 ： 启动宿主应用

```bash
 java -jar kc-sofastack-ark-biz-master/target/kc-sofastack-ark-biz-master-1.0.0.jar 
```

启动成功之后日志信息如下：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*W5hpQbywKr8AAAAAAAAAAABkARQnAQ)

#### 5、Dashboard 管控端注册插件信息

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*EYWFTaHU7bQAAAAAAAAAAABkARQnAQ)

点击新建，弹出注册插件框，输入插件信息和描述信息，执行确定


![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*5VzyQYpx7twAAAAAAAAAAABkARQnAQ)

#### 6、Dashboard 管控端添加版本

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*usVkQKvtHacAAAAAAAAAAABkARQnAQ)

输入版本号和当前版本对应的 ark-biz.jar 的文件地址：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*8fWwSr-RmrYAAAAAAAAAAABkARQnAQ)

此处需要填写文件的绝对路径或者对应的url 资源地址，这里以file协议为例，下图是写者本地打包之后的文件的目录，将此目录填写到上述文件地址一栏即可

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*3qf5SLqdMqYAAAAAAAAAAABkARQnAQ)

保存之后界面展示如下：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*D8xxTrA3ZjcAAAAAAAAAAABkARQnAQ)

#### 7、查看详情 & 推送安装命令

点击上图中的 详情，进入插件详情页

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*UzMtTb8gMPYAAAAAAAAAAABkARQnAQ)

在执行安装之前，可以 先访问下 http://localhost:8800/jvm ，此处因为还没有模块提供 jvm 服务，因此会报错，如下所示：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*6OjsQIZghkUAAAAAAAAAAABkARQnAQ)

然后点击安装

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*p5b0QrNfLmIAAAAAAAAAAABkARQnAQ)

看到器状态会刷新为 RESOLVED ，此时模块正在宿主应用中进行安装操作，

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*5MmiRrbs2FgAAAAAAAAAAABkARQnAQ)

延迟1~2s之后，状态变更为 ACTIVATED ，为激活状态

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*bh2iSrlCUKQAAAAAAAAAAABkARQnAQ)

此时再次访问 http://localhost:8800/jvm 地址，结果如下：

![image.png](https://gw.alipayobjects.com/mdn/rms_1a1552/afts/img/A*lS06S4rNi50AAAAAAAAAAABkARQnAQ)


