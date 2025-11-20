package com.sprint.mission.discodeit.swaggerDocs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false) // 👍👍위의 설정 클래스에 대해서는 프록시가 적용되지 않으며 모든 @Bean 메소드 호출마다 새로운 객체를 생성해준다
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("🍊Discodeit API 문서")
                .description("🍊Discodeit 프로젝트의 Swagger API 문서입니다.")
            )
            .servers(List.of(
                new Server().url("http://localhost:4444").description("로컬 서버")
            ));
    }
}
