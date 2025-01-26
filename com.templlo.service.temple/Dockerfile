# Dockerfile
FROM eclipse-temurin:17-alpine

# 애플리케이션 JAR 파일 복사
COPY ./build/libs/temple-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]