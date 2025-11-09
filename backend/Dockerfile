# 1. 베이스 이미지 선택 (Java 17 환경)
# 본인의 Spring Boot 프로젝트와 Java 버전이 맞는지 확인하세요. (예: 11, 17, 21)
# FROM openjdk:17-slim
FROM eclipse-temurin:17-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 .jar 파일 복사
#    (위 1단계에서 생성된 .jar 파일을 컨테이너의 /app/app.jar 로 복사)
#    [중요] Gradle을 쓴다면 'build/libs/*.jar'가 맞습니다.
COPY build/libs/*.jar app.jar

# 4. 애플리케이션 포트 노출
#    (docker-compose.yml의 "8080:8080"과 일치)
EXPOSE 8080

# 5. 컨테이너 시작 시 앱 실행
#    docker-compose.yml의 환경 변수(DB 정보 등)가 여기서 주입됩니다.
ENTRYPOINT ["java", "-jar", "app.jar"]