package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.SpaCsrfTokenRequestHandler;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NOTE: Hash로 믹스하기떄문에 복호화는 안됨, 그래서 누구라도 이걸 볼수 없게 처리
        return new BCryptPasswordEncoder(); // 비밀번호를 암호화 인코더
    }


    @Bean
    ApplicationRunner dumpSecurityFilters(FilterChainProxy proxy) {
        return args -> {
            System.out.println("=== Security Filters ===");
            proxy.getFilterChains().forEach(chain -> {
                chain.getFilters().forEach(filter ->
                        System.out.println(filter.getClass().getName())
                );
            });
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                .requestMatchers("/api/auth/login").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                );

        return http.build();
    }
}
