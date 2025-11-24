FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests \
    && apt-get remove -y maven \
    && rm -rf /var/lib/apt/lists/*

COPY target/backend-sylkflix-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]