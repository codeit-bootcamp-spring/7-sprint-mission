package com.sprint.mission.discodeit.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    List<Server> servers = List.of(
        new Server().url("http://localhost:8080").description("로컬 개발 서버"),
        new Server().url("https://www.example.com").description("운영 서버(수정 예정)")
    );

    return new OpenAPI()
        .openapi("2.8.9")
        .info(apiInfo())
        .servers(servers)
        .components(new Components());
  }

  private Info apiInfo() {
    return new Info()
        .title("Discodeit API 문서")
        .description("Discodeit 프로젝트의 Swagger API 문서입니다.")
        .version("1.0.0")
        .contact(
            new Contact()
                .name("최지혜")
                .url("https://github.com/ChoiJiHye950")
        )
        .license(
            new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")
        );
  }
}
