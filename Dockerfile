FROM maven:3.9 AS maven
WORKDIR /app
COPY . /app
RUN mvn package

FROM openjdk:20
WORKDIR /app
COPY --from=maven /app/target/security-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]