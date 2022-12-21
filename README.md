# spring-boot-3-template

# 简介

在 spring boot 3 + spring security 6 版本下设计的用户权限模板



# 接口文档

http://host:port/doc.html

超管账号 : administrator

密码 : 123456

# 认证授权架构

## 认证(登陆)

### CSRF校验

对于所有`POST`请求需要进行CSRF校验，需要从`Cookie`中获取名称为`XSRF-TOKEN`的Cookie值，并在请求中携带，携带方式可以为以下其一：

- 放在请求头中，字段名为`X-XSRF-TOKEN` (推荐)
- 放在`Form表单`中，增加一个字段名为`_csrf`

注意：

- 首次访问`Cookie`为空时，可以发送任意`POST`请求，不论成功与否都可以拿到`XSRF-TOKEN`
- 每一次使用`XSRF-TOKEN`都会变化并回写到Cookie中，推荐使用拦截器来执行该操作
- axios已经自动实现了该校验过程：https://www.axios-http.cn/docs/req_config ， `xsrfCookieName`和`xsrfHeaderName`，无需进行配置。

### 认证(登陆)流程

1. 请求接口`/sys/user/token`查询是否之前已经登陆，如果之前没有`XSRF-TOKEN`也可以借此拿到。
2. 请求获取验证码 ， 两个接口二选一。
3. 请求登陆接口，参数见接口文档。



# 后端使用

## 导入

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

## 库表

导入该表即可，其它库表会自动创建： [persistent_logins.sql](persistent_logins.sql)

## 启动

子项目配置文件只需要提供如下内容即可

```yml
system:
  #注册功能开放 关闭后只能由管理员创建新用户
  newUser: true

# 接口文档开关 (生产环境建议关闭)
knife4j:
  enable: true

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

# 前端使用
