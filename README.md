# spring-boot-3-template

用户权限设计模板

spring boot 3
spring security 6

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

