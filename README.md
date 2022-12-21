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

## 授权

### 权限的粒度

1. 粗粒度：用户持有某一个`模块`的所有权限，可以对该模块下的所有资源进行增删改，**这类资源通常直属于系统**；例如论坛版主，版主对帖子有各种操作权限。抽象表述为：对`某一类资源`有`某一权限`。
2. 细粒度：用户持有一部分资源的增删改权限，**这类资源通常归属于系统中的某个实体（模块、版面、用户）**；例如论坛用户，用户可以到不同的版面发表帖子，并且只对自己发布的帖子有修改权限。抽象表述为：对`某一类`中的`特定ID资源`有`某一权限`

其中，前者可以很方便的写入系统的角色表，我们可以规定一个角色`模块管理员`拥有权限`帖子:删除`,`帖子:锁定`,`帖子:隐藏`的权限；这样持有这个角色的用户即对所有帖子有这三种权限。

但是我们很快发现，一般来说还存在**版面的版主**，仅对某一版面内的帖子有权限，即帖子并不是直属于系统的，我们其实希望的是某个用户持有`版面ID=xx 的版主`的角色，该角色拥有对其版面中所有帖子的相应操作权限。我们发现这个角色并不适合写入到系统的角色表中，因为我们不知道它还有没有别的参数，它应该由对应模块来管理。另外需要注意，一般来说不论是哪种版主，都是有期限的，在期限范围内才是有效角色。



总结，判断某个用户是否对某个帖子有修改权限至少有4种条件，满足任意一个即可：

1. 就是帖子作者（细粒度）
2. 是该版面的版主（细粒度）
3. 是超级版主（粗粒度）
4. 是系统超管（粗粒度）



对于前端来说，由于关系到界面功能的显示/隐藏，需要通过请求拿到自己持有的权限，其数据形式根据粒度不同而不同：

- 粗粒度：应当可以直接拿到持有的所有权限（角色、权限）
- 细粒度：条件2情况，应当可以拿到自己持有的所有版主角色的列表；条件1的情况鉴于用户的帖子数量可能非常多，穷举帖子ID可能是不合适的，而且**这部分权限其实也是完全不会由人为操作去修改的**，所以不提供这部分权限信息，由前端自己通过帖子的相关属性判断（需要和后端逻辑一致）

- 

### 粗粒度

粗粒度权限的持有者通常为系统级或模块级的管理员，其角色由系统管理，它的数量非常少并且极少变动

接口`/sys/user/token`的返回值中`authorities`字段成员的`authority`字段值即为`权限字符串`，它是一个**URI**，持有该权限即代表有访问该路径的接口的权限（**且通常不会对请求的参数进行限制**）。 如：持有`/sys/permission/page`即表示持有分页查询权限的权限。

### 细粒度

细粒度权限的持有者通常为 模块下部分资源的管理员 ，其角色由模块管理，角色持有的权限的形式由模块规定。





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
