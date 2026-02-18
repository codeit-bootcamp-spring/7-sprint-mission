package com.sprint.mission.discodeit.global.config.security.filter;

import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtProvider;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtRegistry jwtRegistry;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // 1. Authorization 헤더 없거나 Bearer 아님 → 패스
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Access Token 추출
        String accessToken = authorizationHeader.substring(7);

        // 3. 토큰 유효성 검증
        if (!jwtProvider.validateAccessToken(accessToken)
                && jwtRegistry.hasActiveJwtInformationByAccessToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 토큰에서 사용자 식별자 추출
        String username = jwtProvider.extractSubject(accessToken);

        // 5. UserDetails 로드
        DiscodeitUserDetails userDetails =
                (DiscodeitUserDetails) userDetailsService.loadUserByUsername(username);

        // 6. 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        // 7. SecurityContext에 인증 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 8. 다음 필터
        filterChain.doFilter(request, response);
    }
}
