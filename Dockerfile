FROM amazoncorretto:17-alpine AS build
ARG PROJECT_NAME
ARG PROJECT_VERSION

# 작업 폴더 지정 (이제부터 컨테이너 안의 /app 이라는 폴더에서 작업한다고 명시)
WORKDIR /app

# 이 Dockerfile 기준으로 현재 경로에 있는 모든 파일(소스코드, gradle 등)을 작업폴더 기준인 /app(루트로 설정한 . 경로)으로 복사
COPY . .

# 실행 권한 주기 , +x: execute를 의미함
# chmod를 통해 gradlew wrapper 파일을 실행할 수 있는 권한을 주어야한다.
# 컨테이너 기준 gradlew가 외부 파일이기 때문에 실행할 수 있는 권한이 없다. -> 없다면 Permission Denied 뜸
RUN chmod +x ./gradlew

# gradlew에게 기존에 있던건 지우고(clean) 새롭게 빌드(build)
# 개발 과정에서는 시간 단축 등을 위해 test를 생략하고 빌드 하는것도 가능, 이때 -x test과정을 빼는것이다. 위의 +x 와다르게 -x(exclude)로 빼는것이다.
RUN ./gradlew clean build -x test

######################################

# 두번째 스테이지 -> 실행 영역
FROM amazoncorretto:17-alpine
WORKDIR /app
# 컨테이너 포트 80 명시
EXPOSE 80
ARG PROJECT_NAME
ARG PROJECT_VERSION
# build라는 별칭으로 만들어진 첫번째 스테이지에서 프로젝트이름-버전으로 끝나는 .jar파일을 app.jar로 복사해서 이미지에 세팅
COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 타임존 설정
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata curl

# JVM_OPTS 설정
ENV JVM_OPTS=""

# 이 컨테이너가 시작될 때 무조건 실행해야하는 명령어
# CMD는 기본 실행 명령어를 의미. 컨테이너 실행 시에 다른 명령어가 주어지면 그 명령어로 대체됨.
# ENTRYPOINT는 반드시 실행되어야 할 명령어를 의미. 다른 명령어로 대체되지 않음.
# 스프링 부트는 무조건 -jar 옵션으로 실행되어야 하기에 ENTRYPOINT로 안전하게 선언.
ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar app.jar"]

