# 멀티 스테이지 구성하기  1단계 빌드만
FROM amazoncorretto:17 AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test


# 2단계 실행, jar만 복사하기
FROM amazoncorretto:17

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""
ENV TZ=Asia/Seoul

COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
