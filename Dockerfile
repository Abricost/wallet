FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/wallet-0.0.1-SNAPSHOT.jar /app/wallet-0.0.1-SNAPSHOT.jar

ENV DB_URL=jdbc:postgresql://localhost:5432/postgres
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres

CMD ["java", "-jar", "wallet-0.0.1-SNAPSHOT.jar"]