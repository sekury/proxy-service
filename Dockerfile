# Paketo BellSoft Liberica Buildpack 9.2.0 unable to find dependency no valid dependencies for jdk, 14.*
FROM openjdk:14-alpine as builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:14-alpine
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]