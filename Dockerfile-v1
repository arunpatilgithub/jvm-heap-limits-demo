FROM eclipse-temurin:17 AS builder
WORKDIR workspace
#the below location needs to be different depending on the build tool. Example, maven has different location.
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
#extract layers using layertools jar mode.
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17
WORKDIR workspace
#copy jar layers from the extracted to this workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./

#The Spring Boot fat JarLauncher is extracted from the JAR into the image,
#  so it can be used to start the application without hard-coding the main application class.
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]

#More details on the above are here https://spring.io/guides/topicals/spring-boot-docker/