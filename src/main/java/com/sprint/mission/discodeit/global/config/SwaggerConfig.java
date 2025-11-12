package com.sprint.mission.discodeit.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Discodeit API 문서",
        description = "Discodeit 프로젝트의 Swagger API 문서입니다."
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "로컬 서버"
        )
    }
)
public class SwaggerConfig implements WebMvcConfigurer {

  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
      ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(
        objectMapper);

    // 기존 지원 미디어 타입 복사
    List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());

    // application/octet-stream 허용 추가
    supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);

    // 수정된 리스트를 다시 설정
    converter.setSupportedMediaTypes(supportedMediaTypes);

    return converter;
  }
}
