# ---------- Build Stage ----------
# Amazon Corretto 17를 베이스 이미지로 사용
FROM amazoncorretto:17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 컨테이너로 복사
# .dockerignore에 설정된 파일 제외
COPY . .

# gradle wrapper 파일 실행 권한 설정 및 빌드 실행
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# ---------- Runtime Stage ----------
FROM amazoncorretto:17

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