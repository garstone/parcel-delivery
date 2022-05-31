FROM maven:3.8.5-openjdk-17
COPY src /usr/src/orderservice/src
COPY pom.xml /usr/src/orderservice
RUN mvn -f /usr/src/orderservice/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /usr/src/orderservice/target/orderservice-0.0.1-SNAPSHOT.jar \
/usr/orderservice/orderservice-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java", "jar", "/usr/orderservice/orderservice-0.0.1-SNAPSHOT.jar"]