FROM amazoncorretto:17

WORKDIR /app

# 1. Gradle wrapper
COPY gradlew .
COPY gradle gradle

# 2. 빌드 설정
COPY build.gradle .
COPY settings.gradle .

# 3. 실행 권한 부여 + 의존성 다운로드
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# 4. 소스 코드 복사 + 빌드
COPY src src
RUN ./gradlew clean build -x test --no-daemon

EXPOSE 80

# 프로젝트 정보
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=0.0.1-SNAPSHOT

# JVM 옵션
ENV JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]

