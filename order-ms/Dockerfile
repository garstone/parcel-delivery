FROM openjdk:17.0.1-jdk-slim
COPY ./target/adminservice-0.0.1-SNAPSHOT.jar /usr/adminservice-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "/usr/adminservice-0.0.1-SNAPSHOT.jar"]