FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/dota-0.0.1-SNAPSHOT.jar dota-bags.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["java", "-jar", "dota-bags.jar"]
