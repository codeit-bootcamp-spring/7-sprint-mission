package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final SessionRegistry sessionRegistry;

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
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {

        http.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                                .requestMatchers("/",
                                        "/index.html",
                                        "/favicon.ico",
                                        "/assets/**",
                                        "/error",
                                        "/.well-known/**",
                                        "/api/auth/login",
                                        "/api/auth/csrf-token",
                                        "/api/auth/logout",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/h2-console/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/channels/public").hasRole("CHANNEL_MANAGER") // 웹 레벨(filter)에서 처리, 만약 서비스 유스케이스에 @PreAuthorize 쓰면 메서드보안 레벨이라 전역예외쪽으로 빠짐
                                .requestMatchers(HttpMethod.PATCH, "/api/channels/**").hasRole("CHANNEL_MANAGER")
                                .requestMatchers(HttpMethod.DELETE, "/api/channels/**").hasRole("CHANNEL_MANAGER")
                                .anyRequest().authenticated()
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
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 명시해 놓기
                        .sessionConcurrency(concurrency -> concurrency
                                .sessionRegistry(sessionRegistry)
                                .maximumSessions(1) // 사용자 당 동시 최대 세션수
                                .maxSessionsPreventsLogin(false) // false:새 로그인시 이전 세션 만료
                        )
                        .sessionFixation().changeSessionId() // 명시해놓기, 세션 Id만 변경하고 세션 객체는 그대로 유지
                )
                .rememberMe(remember -> remember
                        .key("spring-mission-remember-me-key")
                        .tokenValiditySeconds(60 * 60 * 24 * 7) // 7일
                        // NOTE: Customizer.withDefaults() 가 기본이라 다 설정되어있지만 바꿀려는부분만 이렇게 key, tokenValiditySeconds를 넣어준것, 저거 안넣어도 기본값으로 알아서 해주긴함
                )

        ;

        return http.build();
    }
}
