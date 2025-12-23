FROM amazoncorretto:17 As build1

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

from amazoncorretto:17

COPY --from=build1 /app/build/libs/*.jar app.jar

ENTRYPOINT ["sh","-c", "java${JVM_OPTS:-}","-jar","app-${PROJECT_NAME:-discodeit}-${PROJECT_VERSION:-latest}.jar"]

ENV TZ = Asia/Seoul

RUN apk add --no-cache curl tzdata