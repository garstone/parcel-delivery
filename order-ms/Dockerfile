FROM openjdk:17.0.1-jdk-slim
COPY ./target/order-ms-0.0.1-SNAPSHOT.jar /usr/order-ms-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "/usr/order-ms-0.0.1-SNAPSHOT.jar"]