FROM eclipse-temurin:17-jdk-alpine as base
WORKDIR /app
COPY .mvn/ ./.mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN chmod +x ./mvnw

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:7999'"]

FROM base as build
RUN ./mvnw clean package -DskipTests
ARG JAR_FILE=target/*.jar
RUN mv ./${JAR_FILE} ./application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17-jre-alpine as production
WORKDIR /app
COPY --from=build app/dependencies/ ./
COPY --from=build app/snapshot-dependencies/ ./
COPY --from=build app/spring-boot-loader/ ./
COPY --from=build app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]