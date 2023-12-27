FROM eclipse-temurin:17-jdk-alpine as base
WORKDIR /app
COPY .mvn/ ./.mvn
COPY mvnw pom.xml ./
COPY src ./src

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base as build
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine as production
EXPOSE 20174
COPY --from=build /app/target/messenger-bot-*.jar /messenger-bot.jar
CMD ["java", "-jar", "messenger-bot.jar"]