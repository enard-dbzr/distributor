FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn -B -q clean package -DskipTests


FROM eclipse-temurin:23-jre
WORKDIR /app

COPY --from=build /app/target/distributor-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]