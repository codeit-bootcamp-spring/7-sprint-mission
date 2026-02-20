package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRegistry jwtRegistry;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null && jwtTokenProvider.isAccessTokenValid(jwt)) {

                if (!jwtRegistry.hasActiveJwtInformationByAccessToken(jwt)) {
                    log.warn("Registry에 존재하지 않는 JWT");
                    filterChain.doFilter(request, response);
                    return;
                }

                authenticateUser(jwt, request);
            }
        } catch (Exception e) {
            log.error("JWT 인증 실패: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        Claims claims = jwtTokenProvider.validateToken(jwt, true);

        UUID userId = UUID.fromString(claims.get("userId", String.class));
        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));

        UserResponseDto dto = new UserResponseDto(
                userId,
                username,
                email,
                null,
                true,
                role
        );

        DiscodeitUserDetails userDetails = new DiscodeitUserDetails(dto, null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // 현재 HTTP 요청(request)에서 사용자의 IP 주소나 기타 요청에 관련된 정보를 추출해서 인증 객체에 Detail로 추가
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Security 컨텍스트에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("JWT 인증 성공: user_id={}, email={}, role={}", userId, email, role);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
