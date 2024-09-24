FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["java", "-jar", "target/currency-gateway-0.0.1-SNAPSHOT.jar"]
