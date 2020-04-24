FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/*.jar app.jar
ADD target/classes/allinpay/prod /root
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Asia/Shanghai","-jar","/app.jar","--spring.profiles.active=test"]