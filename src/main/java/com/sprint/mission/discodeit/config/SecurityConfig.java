package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.security.LoginSuccessHandler;
import com.sprint.mission.discodeit.security.SpaCsrfTokenRequestHandler;
import javax.swing.Spring;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;
import org.springframework.security.web.authentication.ui.DefaultResourcesFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

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
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

//    👉 Spring Boot 3.x / Security 6 기준
//    ✔ BCryptPasswordEncoder
//    ✔ salt 자동 처리
//    ✔ 같은 비밀번호여도 매번 다른 해시 생성
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

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
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스 및 공통 경로 허용
                .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "/favicon.ico").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() //!! 🛠️
                // 인증/인가 관련 API 허용 (예시)
                .requestMatchers("/api/auth/**").permitAll()
                // Swagger UI 및 API 문서 허용
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                // 그 외 모든 요청은 인증 필요
                .requestMatchers("/h2-console/**", "/session-expired").permitAll()
                .requestMatchers("/api/users/**").hasRole("USER")
//                // 그 외 모든 /api/** 요청은 인증 필요
//                .requestMatchers("/api/**").authenticated()
                // 나머지 요청은 모두 허용 (SPA 라우팅 대응)
//                .anyRequest().permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
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
