FROM eclipse-temurin:17 AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

FROM eclipse-temurin:17

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

#ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -jar app.jar"]
#형일님이 알려준 거