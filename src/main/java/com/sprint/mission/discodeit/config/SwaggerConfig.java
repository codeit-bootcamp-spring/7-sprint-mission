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
    public OpenAPI openAPI() {
        List<Server> servers = List.of(
                new Server().url("http://localhost:8080").description("Local server"),
                new Server().url("https://www.SIUUU.com").description("GOAT server"));

        return new OpenAPI()
                .info(apiInfo())
                .servers(servers);

    }

    private Info apiInfo(){
        return new Info()
                .title("Discodeit Sprint Mission API")
                .description("API Document for Discodeit Sprint Mission")
                .version("100.0.0")
                .contact(new Contact()
                        .name("SiUUU Ronaldo Team")
                        .email("Ronaldo@Goat.com")
                )
                .license(new License()
                        .name("SIUUU License"));
    }
}
