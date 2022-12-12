FROM openjdk:18
VOLUME /tmp
ADD spring-boot-3-template-0.0.1-SNAPSHOT.war app.war
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV JAVA_OPTS='--add-opens java.base/java.lang=ALL-UNNAMED'
ENTRYPOINT java ${JAVA_OPTS} -jar /app.war  --spring.profiles.active=prod