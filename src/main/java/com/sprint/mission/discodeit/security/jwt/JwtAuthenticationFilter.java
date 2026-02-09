package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
    private final BinaryContentMapper binaryContentMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);
            if (jwt != null && jwtTokenProvider.isTokenValid(jwt)) {

            }
        } catch (Exception e) {
            log.error("JWT 인증 실패: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        Claims claims = jwtTokenProvider.validateToken(jwt);

        UUID userId = claims.get("userId", UUID.class);
        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        BinaryContent profile = claims.get("profile", BinaryContent.class);
        Role role = claims.get("role", Role.class);

        UserResponseDto dto = new UserResponseDto(
                userId,
                username,
                email,
                binaryContentMapper.toResponseDto(profile),
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
