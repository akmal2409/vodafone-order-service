FROM bellsoft/liberica-openjdk-alpine:19 AS base

ARG JAR_FILE="target/*.jar"

WORKDIR /source

COPY $JAR_FILE app.jar

RUN ["java", "-Djarmode=layertools", "-jar", "app.jar", "extract"]

FROM bellsoft/liberica-openjre-alpine:19 AS build


COPY --from=base /source/dependencies ./
COPY --from=base /source/spring-boot-loader ./
COPY --from=base /source/snapshot-dependencies ./
COPY --from=base /source/application ./


ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]




