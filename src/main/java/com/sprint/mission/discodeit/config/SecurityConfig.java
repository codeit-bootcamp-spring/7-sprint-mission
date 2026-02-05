package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.security.LoginSuccessHandler;
import com.sprint.mission.discodeit.security.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NOTE: Hash로 믹스하기떄문에 복호화는 안됨, 그래서 누구라도 이걸 볼수 없게 처리
        return new BCryptPasswordEncoder(); // 비밀번호를 암호화 인코더
    }


    @Bean
    ApplicationRunner debugUserDetailsService(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("UserDetailsService: " + userDetailsService.getClass());
            System.out.println("passwordEncoder = " + passwordEncoder.getClass());

        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                .requestMatchers("/", "/api/auth/login").permitAll()
                                .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")// H2는 CSRF 검증 제외
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.sameOrigin()) // 클릭재킹 거부(iframe) 같은 주소를 가진 프레임(iframe) 주소라면 허용, 이건 기본 서버사이드 일때 h2화면인 iframe으로 되어있어서 허용할려고하느거고 실제 분리된 프론트에선 피료없음
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                )
        ;

        return http.build();
    }
}
