FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} reservation-service.jar

EXPOSE 19040

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/reservation-service.jar"]