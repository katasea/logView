# BaseFramework


项目简介
---------
提供一个整合完的框架。下载即可开始开发。<br>
1、springboot2.x+ssm+jpa+swagger2+aoplog+auto create table [MYSQL]

2、提供统一接口，调用sdk，含加解密验签功能。 

3、提供Redis缓存集成和Redis分布式锁工具类。

//TODO<br>
1、目前是jpa自动建表，维护表结构还需改进，或者自行引入liquibase建表<br>

2、目前是单应用，可以改造为dubbo多应用<br>

3、可以考虑整合mq/quartz/线程池等。<br>

功能特性
---------

环境依赖
---------
JDK1.8+

部署步骤
---------
下载版本的jar包。启动命令
<br>
```java 
java -jar 编译后的JAR.jar [-Dfile.encoding=utf-8 乱码情况下使用]
     --spring.datasource.username=数据库用户 默认sa
     --spring.datasource.password=数据库密码 默认123
     --druid.loginUsername=Druid登陆用户名 默认slo
     --druid.loginPassword=Druid登陆密码 默认slo
```


声明
---------


协议
---------
GPLv3


