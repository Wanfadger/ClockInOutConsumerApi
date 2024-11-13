FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only the pom.xml first to leverage Docker layer caching
COPY pom.xml .

# Copy the rest of the source code
COPY src ./src

# Build the application
RUN mvn clean install -DskipTests=true


FROM openjdk:21

WORKDIR /app

EXPOSE 8091

# Copy the built artifact from the previous stage
COPY --from=build /app/target/ClockInOutConsumer.jar ClockInOutConsumer.jar

CMD ["java", "-jar", "ClockInOutConsumer.jar"]
