FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY . /app

RUN ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]