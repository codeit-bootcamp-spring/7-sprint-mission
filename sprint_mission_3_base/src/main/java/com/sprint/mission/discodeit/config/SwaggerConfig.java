package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 기본 서버 주소를 /api 로 지정
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Discodeit Local Server")
                ))
                // 문서 정보 (상단 제목/버전/설명/라이선스/작성자 정보)
                .info(new Info()
                        .title("Discodeit Backend Server API 문서")
                        .description("""
                                💬 Codeit Sprint #4 - Discodeit 백엔드 서버 REST API 명세서입니다.
                                아래 목록에서 각 엔드포인트를 직접 테스트할 수 있습니다.
                                """)
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .contact(new Contact()
                                .name("Discodeit 개발팀")
                                .email("support@discodeit.dev")
                                .url("https://github.com/codeitteam/discodeit")));
    }
}
