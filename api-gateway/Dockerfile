FROM openjdk:17.0.1-jdk-slim
COPY ./target/api-gateway-0.0.1-SNAPSHOT.jar /usr/api-gateway-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "/usr/api-gateway-0.0.1-SNAPSHOT.jar"]