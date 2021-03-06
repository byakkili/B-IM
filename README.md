<p align="center">
    <a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.byakkili%22%20AND%20a:%22bim-core%22">
		<img src="https://img.shields.io/maven-central/v/com.github.byakkili/bim-core.svg?label=Maven%20Central" />
	</a>
	<a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0">
        <img src="https://img.shields.io/badge/License-Apache--2.0-brightgreen.svg" />
    </a>
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img src="https://img.shields.io/badge/JDK-1.8+-green.svg" />
    </a>
    <a target="_blank" href="https://app.codacy.com/manual/byakkili/B-IM?utm_source=github.com&utm_medium=referral&utm_content=byakkili/B-IM&utm_campaign=Badge_Grade_Dashboard">
        <img src="https://api.codacy.com/project/badge/Grade/23f223e1ee194b48ab61bfd37e63f6ae" />
    </a>
</p>

-------------------------------------------------------------------------------

## B-IM 简介
B-IM是用JAVA语言，基于Netty实现的轻量级、高性能的IM即时通讯框架，主要是降低相关API的学习成本，降低即时通讯门槛，提高工作效率，让开发者更专注于业务的开发。

## 主要特色
    1、高性能(可支持几十万以上用户同时在线)
    2、轻量，拓展性高(提供丰富的API: 会话监听器、命令拦截器、自定义命令、自定义协议...)
    3、支持集群部署(默认提供redisson实现)
    4、可支持JSON、Protobuf消息格式(默认提供实现，可根据需要自行拓展)

-------------------------------------------------------------------------------

## 包含模块
| 模块                    |     介绍                                        |
|-------------------------|-------------------------------------------------|
| bim-core                | 核心包                                          |
| bim-spring-boot-starter | SpringBoot启动器，让配置更简单                   |

-------------------------------------------------------------------------------

## 文档 
[参考API](https://apidoc.gitee.com/byakkili/B-IM)

-------------------------------------------------------------------------------

## 安装

### Maven
在项目中的pom.xml的dependencies中加入以下内容：
```xml
<dependency>
    <groupId>com.github.byakkili</groupId>
    <artifactId>bim-core</artifactId>
    <version>0.0.3</version>
</dependency>
```

### Gradle
```
compile 'com.github.byakkili:bim-core:0.0.3'
```

### 非Maven项目
点击以下任一链接，下载`bim-core-0.0.3.jar`即可：
- [Maven中央库1](https://repo1.maven.org/maven2/com/github/byakkili/bim-core/0.0.3/)
- [Maven中央库2](http://repo2.maven.org/maven2/com/github/byakkili/bim-core/0.0.3/)

-------------------------------------------------------------------------------

## 使用 
###### (可参考src/test/java下的demo)
```java
public class ServerStarter {
    public static void main(String[] args) {
        BimConfiguration config = new BimConfiguration();
        config.setPort(9000); // 端口
        config.setReaderTimeout(30); // 读超时30秒
        config.setWriterTimeout(30); // 写超时30秒
        
        // 添加协议提供者
        config.addProtocolProvider(new WsProtobufProtocolProvider());
        config.addProtocolProvider(new TcpProtobufProtocolProvider()); 
        
        config.addCmdHandler(new TestHandler()); // 添加命令处理器
        config.addCmdInterceptor(new TestInterceptor()); // 添加命令拦截器
        config.addSessionListener(new TestSessionListener()); // 设置会话监听器
        
        BimNettyServer bimNettyServer = new BimNettyServer(config);
        
        bimNettyServer.start(); // 启动
        
        bimNettyServer.close(); // 关闭
    }
}
```

## DEMO
    1、下载本工程代码
    2、进入目录B-IM\bim-spring-boot-starter\src\test\resources, 修改配置文件application.yml、redisson.yml
    3、运行B-IM\bim-spring-boot-starter\src\test\java\com\github\byakkili\bim\demo\JsonApplication.java的main方法
    4、双击打开B-IM\bim-spring-boot-starter\src\test\resources\index.html即可(可打开多个窗口进行聊天)