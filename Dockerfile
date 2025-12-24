# 첫번째 스테이지 -> 빌드 영역
FROM amazoncorretto:17 as Build

# 작업 폴더 지정 (이제부터 컨테이너 안의 /app 이라는 폴더에서 작업할게!)
WORKDIR /app

# 작업 디렉토리 설정 /app
COPY . .

# gradle wrapper 파일을 실행 권한 부여
RUN chmod +x ./gradlew

# test를 생략하고 빌드
RUN ./gradlew clean build -x test

##############################################3

# 두번째 스테이지 -> 실행 영역
FROM amazoncorretto:17
WORKDIR /app

# build라는 별칭으로 만들어진 첫번째 스테이지에서
# .jar로 끝나는 파일을 app.jar로 복사해서 이미지에 세팅
COPY --from=build /app/build/libs/*.jar ./build/libs/

#환경변수 설정
ENV TZ=Asia/Seoul
ENV JVM_OPTS=""
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

# 80 포트를 노출
EXPOSE 80

ENTRYPOINT exec java -jar ./build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar
