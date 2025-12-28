FROM amazoncorretto:17

WORKDIR /app

COPY . /app

RUN chmod +x ./gradlew && ./gradlew clean build -x test

EXPOSE 80

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# 7. 애플리케이션 실행 명령어 설정
# 환경 변수를 해석하기 위해 'sh -c' 형태를 사용합니다.
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar --spring.profiles.active=prod"]