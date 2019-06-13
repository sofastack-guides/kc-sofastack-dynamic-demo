## SOFABoot 动态模块实践

### 实验背景

对于每个用户而言，购买商品的次数也就体现了用户对于该商品的兴趣程度。对于目前的电商网站或者信息流网站，
绝大多数都会基于此来推荐相应的商品或者信息给用户。这就是我们通常说的千人千面。

本案例将通过 SOFABoot 的动态模块能力，来实现根据用户购买商品的次数来对商品展示的排序顺序进行改变。


### 实验内容

本实验基于 SOFADashboard 完成 SOFABoot 动态模块实践：

* 打包动态模块
* 宿主应用改造
* SOFADashboard 推送命令，改变商品展示顺序


### 架构图

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*6cqeRrAINH8AAAAAAAAAAABkARQnAQ)

### 任务

#### 1、任务准备

从 github 上将 demo 工程克隆到本地

```bash
git clone https://github.com/sofastack-guides/kc-sofastack-dynamic-demo.git
```

然后将工程导入到 IDEA 或者 eclipse。

#### 2、将 SOFABoot 应用打包成 ark 包

在下图所示的工程 pom 配置中，增加 ark 打包插件，并进行配置：

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*YuB-SrpOc5UAAAAAAAAAAABkARQnAQ)

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

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*yFKBR5A5gocAAAAAAAAAAABkARQnAQ)

#### 3、构建宿主应用

在已下载下来的工程中，stock-mng 作为实验的宿主应用工程模型。通过此任务，将 stock-mng  构建成为 动态模块的宿主应用。

- step1 : 引入 ark 动态配置依赖

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*Y14MQ7omf7YAAAAAAAAAAABkARQnAQ)

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
com.alipay.sofa.ark.master.biz=stock-mng
```

- step3 : 配置 ark 打包插件

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*N9peTqFpzloAAAAAAAAAAABkARQnAQ)

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
    <bizName>stock-mng</bizName>
  </configuration>
</plugin>
```

- step4 ： 宿主应用 stock-mng  resource/application.properties 配置文件中添加如下配置项

```properties
#dashboard client config
management.endpoints.web.exposure.include=*
com.alipay.sofa.dashboard.zookeeper.address=zookeeper-1-dev.sofastack.tech:2181
#skip jvm health check to startup host-app
com.alipay.sofa.boot.skip-jvm-reference-health-check=true
```

#### 4、打包宿主应用 & 启动

- step 1 ： mvn clean package 打包
- step2 ： 启动宿主应用 

```bash
 java -jar stock-mng/target/stock-mng-0.0.1-SNAPSHOT.jar 
```

启动成功之后日志信息如下：

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*I2gvQJ4F4m4AAAAAAAAAAABkARQnAQ)

#### 5、Dashboard 管控端注册插件信息

点击新建，弹出注册插件框，输入插件信息和描述信息，执行确定


![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*XIdOSrcQwF8AAAAAAAAAAABkARQnAQ)

#### 6、Dashboard 管控端添加版本

此处需要填写文件的绝对路径或者对应的 url 资源地址，这里以 file 协议为例

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*Mc6ITLOET4MAAAAAAAAAAABkARQnAQ)

#### 7、Dashboard 管控端关联应用

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*PvnQR700gQ8AAAAAAAAAAABkARQnAQ)

#### 8、查看详情 & 推送安装命令

点击上图中的 详情，进入插件详情页

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*9gkxSoxPnqUAAAAAAAAAAABkARQnAQ)

在执行安装之前，可以 先访问下 http://localhost:8080/#list ，此处因为还没有模块提供 jvm 服务，因此展示的是默认的排序顺序，如下所示：

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*I0T_QrXOejoAAAAAAAAAAABkARQnAQ)

然后点击安装，延迟1~2s之后，状态变更为 ACTIVATED ，为激活状态

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*Eft7SbV1xFEAAAAAAAAAAABkARQnAQ)

此时再次访问 http://localhost:8080/#list 地址，结果如下：

![image.png](https://gw.alipayobjects.com/mdn/rms_565baf/afts/img/A*o-a2QKxejnIAAAAAAAAAAABkARQnAQ)


