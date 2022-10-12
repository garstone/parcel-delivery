FROM openjdk:17.0.1-jdk-slim
COPY ./target/tokens-ms-0.0.1-SNAPSHOT.jar /usr/tokens-ms-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "/usr/tokens-ms-0.0.1-SNAPSHOT.jar"]