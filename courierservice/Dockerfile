FROM maven:3.8.5-openjdk-17
COPY src /usr/src/courierservice/src
COPY pom.xml /usr/src/courierservice
RUN mvn -f /usr/src/courierservice/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /usr/src/courierservice/target/courierservice-0.0.1-SNAPSHOT.jar \
/usr/courierservice/courierservice-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "jar", "/usr/courierservice/courierservice-0.0.1-SNAPSHOT.jar"]