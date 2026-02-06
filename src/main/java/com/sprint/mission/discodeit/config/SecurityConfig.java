package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.security.LoginSuccessHandler;
import com.sprint.mission.discodeit.security.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // RoleHierarchy를 매개변수로 선언하면 filterChain에서 권한 계층이 적용됩니다. (빈은 사전에 등록되어있어야 합니다.)
    public SecurityFilterChain filterChain(HttpSecurity http,
                                            RoleHierarchy roleHierarchy,
                                           CustomAccessDeniedHandler accessDeniedHandler, //⭐️ hasRole 검사
                                           CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        http
            // ✅ CSRF 설정
            .csrf(csrf -> csrf
                //디폴트 구현체 HttpSessionCsrfTokenRepository -> 구현체를 CookieCsrfTokenRepository로 설정
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                // 토큰 발급 API는 예외 처리
                .ignoringRequestMatchers(
                    "/api/csrf", "/h2-console/**", "/api/auth/**"
                )
            )
            // ✅ 인증/인가
//            다음의 요청은 인증하지 않도록 설정하세요.
//            Csrf Token 발급
//            회원가입
//            로그인
//            로그아웃
//            API가 아닌 요청(Swagger, Actuator 등)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/csrf-token", "/api/auth"
                ).permitAll() // 토큰 발급용 엔드포인트는 로그인 없이 접근 가능

                .requestMatchers("/actuator/**"
                ).permitAll()

                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"
                ).permitAll()

                .requestMatchers("/h2-console/**", "/session-expired"
                ).permitAll()

                // 정적 리소스 및 공통 경로 허용
                .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "/favicon.ico"
                ).permitAll()


                .requestMatchers("/api/users/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() //!! 🛠️
//                // 인증/인가 관련 API 허용 (예시)
//                .requestMatchers("/api/auth/**").permitAll()
//                // Swagger UI 및 API 문서 허용
//                // 그 외 모든 요청은 인증 필요
                .requestMatchers("/api/users/**").hasRole("USER")  //⭐️ AccessDeniedHandler 에서 검사
////                // 그 외 모든 /api/** 요청은 인증 필요
////                .requestMatchers("/api/**").authenticated()
//                // 나머지 요청은 모두 허용 (SPA 라우팅 대응)
////                .anyRequest().permitAll()
//                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint) // 인증
                .accessDeniedHandler(accessDeniedHandler) // 인가
            )

//            DEBUG o.s.s.web.DefaultSecurityFilterChain - Will secure any request with filters: DisableEncodeUrlFilter, WebAsyncManagerIntegrationFilter, SecurityContextHolderFilter, HeaderWriterFilter, CsrfFilter, LogoutFilter,                                                                                                                                    RequestCacheAwareFilter, SecurityContextHolderAwareRequestFilter, AnonymousAuthenticationFilter, ExceptionTranslationFilter, AuthorizationFilter
//            .formLogin(form -> form.disable()) // ✅ 로그인 비활성화 (CSR이니까)
            // formLogin 을 기본값으로 활성화하고, 추가된 필터를 확인해보세요.
//            .formLogin(Customizer.withDefaults())
//            DEBUG o.s.s.web.DefaultSecurityFilterChain - Will secure any request with filters: DisableEncodeUrlFilter, WebAsyncManagerIntegrationFilter, SecurityContextHolderFilter, HeaderWriterFilter, CsrfFilter, LogoutFilter, UsernamePasswordAuthenticationFilter, DefaultResourcesFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter, RequestCacheAwareFilter, SecurityContextHolderAwareRequestFilter, AnonymousAuthenticationFilter, ExceptionTranslationFilter, AuthorizationFilter

            .formLogin(login -> login
                .loginProcessingUrl("/api/auth/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")   // ⭐ 핵심
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)) // 204
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                .permitAll()
            )
            .httpBasic(basic -> basic.disable())

            // ✅ H2 콘솔용
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
