FROM amazoncorretto:17 AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew --no-daemon clean bootJar -x test
# RUN ./gradlew clean build -x test


FROM amazoncorretto:17

WORKDIR /app

ENV TZ=Asia/Seoul \
    PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS="" \
    SERVER_PORT=80

#RUN apk add --no-cache curl tzdata

EXPOSE 80

ARG PROJECT_NAME=discodeit
ARG PROJECT_VERSION=1.2-M8

COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar

CMD ["sh", "-c", "exec java $JVM_OPTS -jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar --server.port=${SERVER_PORT}"]

#ENTRYPOINT ["java","-jar","app.jar"]