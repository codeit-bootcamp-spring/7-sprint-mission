FROM nginx:alpine

# 기본 설정 파일 제거
RUN rm /etc/nginx/conf.d/default.conf

# 커스텀 설정 파일 복사
COPY conf/default.conf /etc/nginx/conf.d/

# 정적 파일을 위한 디렉토리 생성 (옵션)
RUN mkdir -p /usr/share/nginx/html/static

# 인증서를 위한 디렉토리 생성
RUN mkdir -p /etc/nginx/certs

COPY ./frontend-dist /usr/share/nginx/html

EXPOSE 80