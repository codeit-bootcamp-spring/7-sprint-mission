# Nginx API Gateway Dockerfile
FROM nginx:alpine

# 메타데이터
LABEL maintainer="Codeit"
LABEL description="API Gateway using Nginx for Microservices"
LABEL version="1.0.0"

# 필요한 패키지 설치
RUN apk add --no-cache curl

# 기본 설정 제거
RUN rm /etc/nginx/conf.d/default.conf

# 커스텀 설정 복사
COPY conf/api-gateway.conf /etc/nginx/conf.d/

# HTML 파일들 복사
COPY html/ /usr/share/nginx/html/

# 로그 디렉터리 권한 설정
RUN mkdir -p /var/log/nginx && \
    chown -R nginx:nginx /var/log/nginx

# 포트 노출
EXPOSE 80

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost/health || exit 1

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]