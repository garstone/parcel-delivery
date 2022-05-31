FROM maven:3.8.5-openjdk-17
COPY src /usr/src/adminservice/src
COPY pom.xml /usr/src/adminservice
RUN mvn -f /usr/src/adminservice/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /usr/src/adminservice/target/adminservice-0.0.1-SNAPSHOT.jar \
/usr/adminservice/adminservice-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "jar", "/usr/adminservice/adminservice-0.0.1-SNAPSHOT.jar"]