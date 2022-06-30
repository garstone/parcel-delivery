FROM openjdk:17.0.1-jdk-slim
COPY ./target/identityservice-0.0.1-SNAPSHOT.jar /usr/identityservice-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "/usr/identityservice-0.0.1-SNAPSHOT.jar"]