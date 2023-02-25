FROM openjdk:18
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#安装 mysql client 用于数据库备份、还原
RUN curl -fL -O https://downloads.mysql.com/archives/get/p/23/file/mysql-community-client-8.0.30-1.el8.x86_64.rpm \
    -O https://downloads.mysql.com/archives/get/p/23/file/mysql-community-client-plugins-8.0.30-1.el8.x86_64.rpm \
    -O https://downloads.mysql.com/archives/get/p/23/file/mysql-community-common-8.0.30-1.el8.x86_64.rpm \
    -O https://downloads.mysql.com/archives/get/p/23/file/mysql-community-libs-8.0.30-1.el8.x86_64.rpm \
       && rpm -ivh mysql-community-* && rm mysql-community-*
ENV JAVA_OPTS='--add-opens java.base/java.lang=ALL-UNNAMED'
# 中文字体，用于生成中文校验码
ADD simhei.ttf  /usr/share/fonts/ttf-dejavu/simhei.ttf
ADD spring-boot-3-template-0.0.1-SNAPSHOT.war app.war
ENTRYPOINT java ${JAVA_OPTS} -jar /app.war  --spring.profiles.active=prod