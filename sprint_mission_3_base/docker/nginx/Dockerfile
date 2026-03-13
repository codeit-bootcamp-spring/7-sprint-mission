FROM nginx:1.27-alpine
COPY docker/nginx/nginx.conf /etc/nginx/nginx.conf
COPY src/main/resources/static /usr/share/nginx/html
EXPOSE 3000