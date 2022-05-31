FROM maven:3.8.5-openjdk-17
COPY src /usr/src/userservice/src
COPY pom.xml /usr/src/userservice
RUN mvn -f /usr/src/userservice/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /usr/src/userservice/target/userservice-0.0.1-SNAPSHOT.jar \
/usr/userservice/userservice-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "jar", "/usr/userservice/userservice-0.0.1-SNAPSHOT.jar"]