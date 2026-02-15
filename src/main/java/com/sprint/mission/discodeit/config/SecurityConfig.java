package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.jpa.JpaPersistentTokenRepository;
import com.sprint.mission.discodeit.security.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.security.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.trash.LoginSuccessHandler;
import com.sprint.mission.discodeit.security.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

//    private final LoginSuccessHandler loginSuccessHandler;
    private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final SessionRegistry sessionRegistry;
    private final DiscodeitUserDetailsService userDetailsService;
    private final JpaPersistentTokenRepository persistentTokenRepository;

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
                                           CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        http
            // ✅ CSRF 설정. 쿠키 쓸 경우  = 위조 링크 클릭 -> 쿠키 전달해서 -> 위조된 요청보내
            .csrf(csrf -> csrf
                //디폴트 구현체 HttpSessionCsrfTokenRepository -> 구현체를 CookieCsrfTokenRepository로 설정
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                // 토큰 발급 API는 예외 처리
//                .ignoringRequestMatchers(
//                    "/api/csrf", "/h2-console/**", "/api/auth/**"
//                )
            )
            // ✅ 인증/인가
//            다음의 요청은 인증하지 않도록 설정하세요.
//            Csrf Token 발급
//            회원가입
//            로그인
//            로그아웃
//            API가 아닌 요청(Swagger, Actuator 등)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/csrf-token"
                ).permitAll() // 토큰 발급용 엔드포인트는 로그인 없이 접근 가능

                .requestMatchers("/actuator/**"
                ).permitAll()

                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"
                ).permitAll()

                .requestMatchers("/h2-console/**"
                ).permitAll()

                // 정적 리소스 및 공통 경로 허용
                .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "/favicon.ico"
                ).permitAll()

//                .requestMatchers("/api/users/**").hasRole("USER")  //⭐️ AccessDeniedHandler 에서 검사 {"code":"401","message":"NO_LOGIN"} 떠야해!
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
                .successHandler(jwtLoginSuccessHandler)
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

            .headers(headers -> headers
                // ✅ H2 콘솔용 = 같은 사이트에서 iframe을 사용하는 것을 허용해 달라.
                .frameOptions(frame -> frame.sameOrigin())
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT는 세션 안씀
//                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                    .invalidSessionUrl("/session-expired") // 세션 만료시 이동할 URL

//                    // 동시성 관련 설정은 이 블록 안에서 작성한다. JWT 에선 삭제
//                    .sessionConcurrency(concurrency -> concurrency
//                        .maximumSessions(1) // 한 사용자 당 최대 세션 수
//                        .maxSessionsPreventsLogin(false) // false: 새 로그인 시 이전 세션 만료, true: 이미 로그인 되어있다면 새 로그인 차단
//                        .sessionRegistry(sessionRegistry)
////                        .expiredUrl("/session-expired")
//                    )

                    .sessionFixation().changeSessionId() // 세션 ID만 변경하고 세션 객체는 그대로 유지
//                        .sessionFixation().migrateSession() // 새 세션을 생성해서 기존 세션의 모든 속성을 복사 후 기존 세션 무효화
//                        .sessionFixation().newSession() // 새 세션을 생성, 기존 데이터 유지되지 않음!
//                        .sessionFixation().none() // 사용하지 마세요. 아무것도 안 합니다.
            )
            .rememberMe(remember -> remember
                .rememberMeCookieName("remember-me") // 쿠키 이름
                .rememberMeParameter("remember-me") // HTML 폼 파라미터 이름 (체크박스의 name과 정확히 일치하게 작성!)
                .tokenValiditySeconds(60 * 60 * 24 * 14) // 14일
                .userDetailsService(userDetailsService)
                .tokenRepository(persistentTokenRepository)
                .key(rememberMeKey)
            );

        return http.build();
    }
}