FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/client-0.0.1-SNAPSHOT.jar client.jar
EXPOSE 3000
ENTRYPOINT ["java","-jar","client.jar"]
