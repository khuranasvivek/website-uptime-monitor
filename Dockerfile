FROM maven:3.5-jdk-8-alpine AS mavenbuild
COPY src /usr/app/utm/src
COPY pom.xml /usr/app/utm
RUN mvn -f /usr/app/utm/pom.xml clean install

FROM openjdk:8-jre-alpine
WORKDIR /usr/app/utm
COPY --from=mavenbuild /usr/app/utm/target/uptime-monitor-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]