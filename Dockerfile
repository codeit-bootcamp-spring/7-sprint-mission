#첫 번째 스테이지 -> 빌드 영역
#베이스 이미지 eclipse-temurin:17
#이미지 빌드 시 자바 17버전 설치된 리눅스 환경
FROM eclipse-temurin:17 AS build

#작업 폴더 지정 (컨테이너 안의 /app 폴더에서 작업)
WORKDIR /app

# Dockerfile을 기준으로 현재 경로에 있는 모든 파일(소스 코드, gradle 등)을
# 작업 폴더(/app)로 복사
COPY . .

# RUN -> 리눅스 명령어 실행
# chmod -> gradle wrapper 파일 실행 권한
# 컨테이너에겐 gradlew가 외부 파일, 권한 x이기 때문
RUN chmod +x ./gradlew

# clean -> build
# -x test -> test 생략
RUN ./gradlew clean build -x test

#================================================================
#두 번째 스테이지 -> 실행 영역
FROM eclipse-temurin:17

#build(첫 번째 스테이지)에서 만들어진 *.jar 파일 -> app.jar 복사, 이미지 세팅
COPY --from=build /app/build/libs/*.jar app.jar

# when contrainer starts, must 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]

# CMD는 기본 실행 명령어를 의미. 컨테이너 실행 시에 다른 명령어가 주어지면 그 명령어로 대체됨.
# ENTRYPOINT는 반드시 실행되어야 할 명령어를 의미. 다른 명령어로 대체되지 않음.
# 스프링 부트는 무조건 -jar 옵션으로 실행되어야 하기에 ENTRYPOINT로 안전하게 선언.