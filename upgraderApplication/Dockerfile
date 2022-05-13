FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} upgrader-service.jar
ENTRYPOINT ["java", "-jar", "/upgrader-service.jar"]
EXPOSE 9001