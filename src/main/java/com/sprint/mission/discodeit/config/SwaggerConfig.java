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
  public OpenAPI discodeitOpenAPI() {
    // Swagger UI 상에서 선택할 서버 목록 (8080만 볼 것이니 추가로 운영서버나 이런거 생성X)
    List<Server> servers = List.of(
        new Server()
            .url("http://localhost:8080")
            .description("Discodeit 로컬 프리서버 입니다!")
    );

    return new OpenAPI()
        .info(apiInfo())
        .servers(servers)
        .components(new Components());
  }

  private Info apiInfo() {
    return new Info()
        .title("Discodeit REST API Documentation 입니다.")
        .description("""
            Discodeit 프로젝트의 사용자, 채널, 메시지, 읽음 상태, 바이너리 파일 관련 REST API 문서입니다.
            
            springdoc-openApi와 Swagger-UI를 활용해 자동 생성된 스펙을 확인하고, 각 엔드포인트를 바로 테스트할 수 있습니다.
            """)
        .version("1.0.0")
        .contact(new Contact()
            .name("Discodeit Backend Team")
            .email("dev@team3.co.kr"))
        .license(new License()
            .name("MIT License")
            .url("https://opensource.org/licenses/MIT"));
  }
}