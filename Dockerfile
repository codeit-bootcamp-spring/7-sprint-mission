# Stage 1: Build Stage
FROM amazoncorretto:17-al2-jdk AS builder
WORKDIR /app

# 1. 의존성 캐싱을 위해 설정 파일만 먼저 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 2. 라이브러리 다운로드 (레이어 캐싱 활용)
# 소스 코드가 없어도 의존성만 미리 받아두면 코드 수정 시 빌드 속도가 빨라집니다.
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 3. 전체 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Runtime Stage (Slim Image)
# 빌드 도구가 없는 가벼운 JRE(또는 JDK-slim) 버전을 사용합니다.
FROM amazoncorretto:17-al2-jdk AS runtime
WORKDIR /app

# 4. 빌드 스테이지에서 생성된 JAR 파일만 추출
# 환경 변수 PROJECT_NAME, VERSION은 하드코딩보다 빌드 시점에 유연하게 대처 가능합니다.
COPY --from=builder /app/build/libs/discodeit-1.2-M8.jar app.jar

EXPOSE 80

ENV JVM_OPTS="-Xms512m -Xmx512m"

# 5. 애플리케이션 실행 명령어
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar app.jar --spring.profiles.active=prod"]