# spring-boot-3-template

用户权限设计模板

spring boot 3
spring security 6

# 导入

```xml

<project>
    <repositories>
        <repository>
            <id>com.gin</id>
            <name>spring-boot-3-template</name>
            <url>https://agmonk.github.io/spring-boot-3-template/maven-repo/</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>com.gin</groupId>
        <artifactId>spring-boot-3-template</artifactId>
        <version>1.0.0</version>
    </dependency>
</project>
```

# 库表

导入该表即可，其它库表会自动创建： [persistent_logins.sql](persistent_logins.sql)

# 启动

子项目配置文件只需要提供如下内容即可

```yml
system:
  #注册功能开放 关闭后只能由管理员创建新用户
  newUser: true

logging:
  level:
    #本模板的日志打印级别
    com.gin: debug

spring:
  datasource:
    #数据库连接: 地址,端口,数据库名
    url: jdbc:mysql://localhost:3306/spring-boot-3-template?serverTimezone=GMT%2B8&characterEncoding=utf-8&allowMultiQueries=true
    username: 数据库用户名
    password: 数据库密码
  data:
    redis:
      host: redis服务器ip
      password: redis密码
      database: 数据库序号

```

启动类上需要添加注解

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.gin"})
@MapperScan(basePackages = {"com.gin.springboot3template.sys.dao"})
```

