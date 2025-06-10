FROM eclipse-temurin:21-jre-jammy
WORKDIR /demo
COPY target/demo-1.0.0.jar demo-1.0.0.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo-1.0.0.jar"]