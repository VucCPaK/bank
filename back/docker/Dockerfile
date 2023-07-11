FROM openjdk:17
COPY ../target/*.jar /app/back.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "back.jar"]