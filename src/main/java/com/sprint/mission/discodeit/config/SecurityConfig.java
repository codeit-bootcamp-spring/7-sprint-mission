package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    // 6️⃣ 비밀번호 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 1️⃣ CSRF 설정 (세션 기반이지만 과제/API 편의를 위해 비활성화)
            .csrf(csrf -> csrf.disable())

            // 2️⃣ 인가 규칙 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/logout", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )

            // 3️⃣ 폼 로그인 설정 (세션 기반)
            .formLogin(form -> form
                .loginPage("/login")          // 기본 로그인 페이지 사용
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            // 4️⃣ 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )

            // 5️⃣ 세션 관리
            .sessionManagement(session -> session
                .sessionFixation().migrateSession()
            );

        return http.build();
    }
}
