FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app
COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/dota-0.0.1-SNAPSHOT.jar dota-bags.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["java", "-jar", "dota-bags.jar"]
