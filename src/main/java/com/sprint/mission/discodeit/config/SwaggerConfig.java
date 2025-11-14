package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.Components;
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
    public OpenAPI openAPI() {
        List<Server> servers = List.of(
                new Server()
                        .url("http://localhost:8080")
                        .description("Sprint Mission 5 - API Documentation")
        );
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers)
                .components(new Components());
    }

    private Info apiInfo() {
        return new Info()
                .title("Codeit Sprint Mission Discodeit System API")
                .description("Discord를 모방한 채팅 기능에 관한 여러가지 기능을 제공하는 API 입니다.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Codeit 7th Developer 이형일")
                        .email("eldh1000@naver.com"))
                .license(new License()
                        .name("There is no License")
                        .url(""));
    }
}
