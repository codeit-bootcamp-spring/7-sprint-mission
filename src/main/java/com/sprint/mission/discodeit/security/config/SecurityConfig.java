package com.sprint.mission.discodeit.security.config;

import com.sprint.mission.discodeit.security.*;
import com.sprint.mission.discodeit.security.jwt.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.security.repository.JpaPersistentTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           LoginFailureHandler loginFailureHandler,
                                           CustomAuthenticationEntryPoint authenticationEntryPoint,
                                           CustomAccessDeniedHandler accessDeniedHandler,
                                           DiscodeitUserDetailsService userDetailsService,
                                           JpaPersistentTokenRepository persistentTokenRepository,
                                           JwtLoginSuccessHandler jwtLoginSuccessHandler) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/index.html", "/assets/**").permitAll()
                        .requestMatchers("/api/auth/csrf-token", "/api/auth/login", "/api/auth/logout").permitAll()
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/swagger-ui/**", "/actuator/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        .requestMatchers("/api/auth/role").hasRole("ADMIN") // 사용자 역할 변경
                        .requestMatchers("/api/channels/public").hasRole("CHANNEL_MANAGER") // 공개 채널 생성
                        .requestMatchers(HttpMethod.PUT, "/api/channels").hasRole("CHANNEL_MANAGER") // 공개 채널 수정

                        .anyRequest().authenticated() // 모든 요청을 인증하도록 설정
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login") // 로그인 처리 url
                        .successHandler(jwtLoginSuccessHandler) // 로그인 인증 성공
                        .failureHandler(loginFailureHandler) // 로그인 인증 실패
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 기본 구현체 : HttpSessionCsrfTokenRepository
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .rememberMe(remember -> remember
                        .key("DiscodeitRememberMeSecretKey2026")
                        .tokenValiditySeconds(60 * 60 * 24 * 14)
                        .userDetailsService(userDetailsService)
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("remember-me")
                        .useSecureCookie(false) // 개발 환경
                        .tokenRepository(persistentTokenRepository)
                );

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
