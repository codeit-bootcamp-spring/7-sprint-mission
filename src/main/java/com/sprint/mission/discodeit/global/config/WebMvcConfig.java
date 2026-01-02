package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.interceptor.LoggingInterceptor;
import com.sprint.mission.discodeit.global.interceptor.MDCLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MDCLoggingInterceptor())
                .addPathPatterns("/api/**");

        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns("/api/**");
    }
}
