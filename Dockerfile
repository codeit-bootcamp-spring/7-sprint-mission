# ---------- Build Stage ----------
# Amazon Corretto 17를 베이스 이미지로 사용
FROM eclipse-temurin:17-jdk AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 의존성 파일 복사
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# gradle wrapper 파일 실행 권한 설정
RUN chmod +x ./gradlew

# gradle 의존성 다운로드 (캐싱)
RUN ./gradlew dependencies --no-daemon

# 소스코드 복사
COPY src ./src

#빌드 실행
RUN ./gradlew clean build -x test --no-daemon

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 정보 환경 변수
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

# JVM 옵션 (기본값 빈 문자열)
ENV JVM_OPTS=""

# 빌드된 jar 파일 복사
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 80 포트 노출 설정 문서화
EXPOSE 80

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]