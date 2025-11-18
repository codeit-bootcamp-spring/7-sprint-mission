package com.sprint.mission.discodeit.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = List.of(new Server().url("http://localhost:8080")
                .description("로컬 개발 서버"));
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers)
                .components(new Components());
    }

    private Info apiInfo() {
        return new Info()
                .title("채팅 시스템")
                .version("1.0.0")
                .contact(new Contact().name("코드잇 7기 김지예"));
    }
}
