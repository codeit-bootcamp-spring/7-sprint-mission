package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.jwt.Http403ForbiddenAccessDeniedHandler;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.security.jwt.JwtLogoutHandler;
import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.security.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final SessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    @Value("${security.remember-me.key}")
    private String rememberMeKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // RoleHierarchy를 매개변수로 선언하면 filterChain에서 권한 계층이 적용됩니다. (빈은 사전에 등록되어있어야 합니다.)
    public SecurityFilterChain filterChain(HttpSecurity http,
                                            RoleHierarchy roleHierarchy,
                                           CustomAccessDeniedHandler accessDeniedHandler, //⭐️ hasRole 검사
                                           CustomAuthenticationEntryPoint authenticationEntryPoint,
                                           JwtAuthenticationFilter jwtAuthenticationFilter,
                                           JwtLoginSuccessHandler jwtLoginSuccessHandler,
                                           JwtLogoutHandler jwtLogoutHandler) throws Exception {

        http
            // JWT - CSRF 비활성화
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
            )
            .formLogin(login -> login
                .loginProcessingUrl("/api/auth/login")
                .successHandler(jwtLoginSuccessHandler)
                .failureHandler(loginFailureHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(jwtLogoutHandler)
                .logoutSuccessHandler(
                    new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                .deleteCookies("REFRESH_TOKEN")
            )
            // ✅ H2 콘솔용 = 같은 사이트에서 iframe을 사용하는 것을 허용해 달라.
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//            .csrf(AbstractHttpConfigurer::disable)
            //JWT - HTTP 기본 인증 방식을 사용하지 않겠다.
//            .httpBasic(AbstractHttpConfigurer::disable)
            // ✅ 인증/인가
//            다음의 요청은 인증하지 않도록 설정하세요.
//            Csrf Token 발급
//            회원가입
//            로그인
//            로그아웃
//            API가 아닌 요청(Swagger, Actuator 등)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/auth/csrf-token"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/users"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/login"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/refresh"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/logout"),

                    new NegatedRequestMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
                ).permitAll()

                .anyRequest().authenticated()
            )
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
//                    .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
//
//                    .requestMatchers("/api/auth/csrf-token"
//                    ).permitAll() // 토큰 발급용 엔드포인트는 로그인 없이 접근 가능
//
//                    .requestMatchers("/actuator/**"
//                    ).permitAll()
//
//                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"
//                    ).permitAll()
//
//                    .requestMatchers("/h2-console/**"
//                    ).permitAll()
//
//                    // 정적 리소스 및 공통 경로 허용
//                    .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "/favicon.ico"
//                    ).permitAll()
//
////                .requestMatchers("/api/users/**").hasRole("USER")  //⭐️ AccessDeniedHandler 에서 검사 {"code":"401","message":"NO_LOGIN"} 떠야해!
//////                // 그 외 모든 /api/** 요청은 인증 필요
//////                .requestMatchers("/api/**").authenticated()
////                // 나머지 요청은 모두 허용 (SPA 라우팅 대응)
//////                .anyRequest().permitAll()
////                // 그 외 모든 요청은 인증 필요
//                    .anyRequest().authenticated()
//            )
//            .exceptionHandling(ex -> ex
//                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증
//                .accessDeniedHandler(accessDeniedHandler) // 인가
//            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .accessDeniedHandler(new Http403ForbiddenAccessDeniedHandler(objectMapper))
            )
            // JWT 세션 사용 안함
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}