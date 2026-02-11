package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.*;
import com.sprint.mission.discodeit.security.filter.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.security.jwt.JwtLogoutHandler;
import com.sprint.mission.discodeit.security.service.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.security.service.LoginFailureHandler;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

        @Slf4j
        @Configuration
        @EnableMethodSecurity
        @RequiredArgsConstructor
        public class SecurityConfig {
            private final LoginFailureHandler loginFailureHandler;
            private final JwtLoginSuccessHandler  jwtLoginSuccessHandler;
            private final JwtLogoutHandler jwtLogoutHandler;
            private final HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler;
            private final DiscodeitAccessDeniedHandler accessDeniedHandler;
            private final DiscodeitAuthenticationEntryPoint authenticationEntryPoint;
            private final SessionRegistry sessionRegistry;
            private final DiscodeitUserDetailsService  userDetailsService;
            private final JwtAuthenticationFilter jwtAuthenticationFilter;

            @Value("${rememberme.key}")
            private String rememberMeKey;

            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http, RoleHierarchy roleHierarchy) throws Exception {
                http
                        .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                        .ignoringRequestMatchers("/h2-console/**","/")
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(jwtLoginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .addLogoutHandler(jwtLogoutHandler)

                )
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/refresh").permitAll()
                        .requestMatchers("/api/auth/csrf-token").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin())
                )
        ;
                DefaultSecurityFilterChain chain = http.build();
                for (Filter filter:chain.getFilters()){
                    log.info("기본 필터 리스트 : {}",filter.getClass().getName());
                }
                return chain;
    }


}
