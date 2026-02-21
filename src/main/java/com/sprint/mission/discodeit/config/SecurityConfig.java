package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.security.JwtLogoutHandler;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

// DEBUG o.s.s.web.DefaultSecurityFilterChain - Will secure any request with filters:
// DisableEncodeUrlFilter,
// WebAsyncManagerIntegrationFilter,
// SecurityContextHolderFilter,
// HeaderWriterFilter,
// CsrfFilter,
// LogoutFilter,
// UsernamePasswordAuthenticationFilter,
// DefaultResourcesFilter,
// DefaultLoginPageGeneratingFilter,
// DefaultLogoutPageGeneratingFilter,
// RequestCacheAwareFilter,
// SecurityContextHolderAwareRequestFilter,
// AnonymousAuthenticationFilter,
// ExceptionTranslationFilter,
// AuthorizationFilter


@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

//    private final LoginSuccessHandler loginSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final SessionRegistry sessionRegistry;
//    private final DiscodeitUserDetailsService userDetailsService;
//    private final JpaPersistentTokenRepository persistentTokenRepository;

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
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/auth/csrf-token"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/users"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/login"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/refresh"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/logout"),

                    //💙 공개 엔드포인트는 반드시 permitAll() 설정
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/auth/**"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/users/register"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/**"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/users/register"),
                    new NegatedRequestMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
                ).permitAll()
                .anyRequest().authenticated()
            )

//            DEBUG o.s.s.web.DefaultSecurityFilterChain - Will secure any request with filters: DisableEncodeUrlFilter, WebAsyncManagerIntegrationFilter, SecurityContextHolderFilter, HeaderWriterFilter, CsrfFilter, LogoutFilter,                                                                                                                                    RequestCacheAwareFilter, SecurityContextHolderAwareRequestFilter, AnonymousAuthenticationFilter, ExceptionTranslationFilter, AuthorizationFilter
//            .formLogin(form -> form.disable()) // ✅ 로그인 비활성화 (CSR이니까)
            // formLogin 을 기본값으로 활성화하고, 추가된 필터를 확인해보세요.
//            .formLogin(Customizer.withDefaults())
//            DEBUG o.s.s.web.DefaultSecurityFilterChain - Will secure any request with filters: DisableEncodeUrlFilter, WebAsyncManagerIntegrationFilter, SecurityContextHolderFilter, HeaderWriterFilter, CsrfFilter, LogoutFilter, UsernamePasswordAuthenticationFilter, DefaultResourcesFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter, RequestCacheAwareFilter, SecurityContextHolderAwareRequestFilter, AnonymousAuthenticationFilter, ExceptionTranslationFilter, AuthorizationFilter

            .formLogin(login -> login
                .loginProcessingUrl("/api/auth/login")
                .successHandler(jwtLoginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(jwtLogoutHandler)
                .logoutSuccessHandler(
                    new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
            )

            // CORS 설정
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint) // 인증
                .accessDeniedHandler(accessDeniedHandler) // 인가
            )
//            .exceptionHandling(ex -> ex
//                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
//                .accessDeniedHandler(new Http403ForbiddenAccessDeniedHandler(objectMapper))
//            )
            // ✅ H2 콘솔용 = 같은 사이트에서 iframe을 사용하는 것을 허용해 달라.
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            // JWT 세션 사용 안함
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//            .rememberMe(remember -> remember
//                .rememberMeCookieName("remember-me") // 쿠키 이름
//                .rememberMeParameter("remember-me") // HTML 폼 파라미터 이름 (체크박스의 name과 정확히 일치하게 작성!)
//                .tokenValiditySeconds(60 * 60 * 24 * 14) // 14일
//                .userDetailsService(userDetailsService)
//                .tokenRepository(persistentTokenRepository)
//                .key(rememberMeKey)
//            );

        return http.build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setExposedHeaders(List.of("Authorization"));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/api/**", configuration);
//        return source;
//    }
}