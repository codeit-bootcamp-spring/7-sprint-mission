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
    public OpenAPI customOpenAPI() {
        List<Server> servers = List.of(new Server().url("http://localhost:8080").description("로컬개발서버"),
                new Server().url("http://www.example.com").description("운영서버(예정)")
        );
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers)
                .components(new Components());
    }
    //아이오점스웨거 점 모델 점의 인포
    private Info apiInfo(){
        return new Info()
                //커스텀이고 대략가능하다
                .title("스프링미션5")
                .description("스프링미션5 스웨거")
                .version("1.0.0")
                .contact(new Contact()
                        .name("신제원")
                        .email("shinjawon@navver.comm"))
                .license(new License()
                        //오프놋스
                        .name("MIT License")
                        .url("https://opensouerc.org/licenses/MIT")


                );

    }
}
