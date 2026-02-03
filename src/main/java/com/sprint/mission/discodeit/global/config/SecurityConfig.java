package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.config.security.handler.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.global.config.security.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.global.config.security.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.global.config.security.handler.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final SessionRegistry sessionRegistry;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/csrf-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers("/api/auth/login", "/api/auth/logout").permitAll()
                        .requestMatchers("/swagger-ui.html", "/actuator", "/actuator/**").permitAll()
                        .requestMatchers("/", "/assets/**", "/index.html", "/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler))
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session -> session
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1) // 한 사용자 당 최대 세션 수
                                .maxSessionsPreventsLogin(false) // 새 로그인 시 이전 세션 만료, true: 새 로그인 시 차단
                                .sessionRegistry(sessionRegistry)
                        )
                );

        return http.build();
    }
}
