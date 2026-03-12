package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRegistry jwtRegistry;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            try {
                String authHeader = accessor.getFirstNativeHeader("Authorization");

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new IllegalArgumentException("Invalid Authorization header");
                }

                String jwt = authHeader.substring(7);

                if (!jwtTokenProvider.isAccessTokenValid(jwt)) {
                    throw new IllegalArgumentException("Invalid JWT token");
                }

                if (!jwtRegistry.hasActiveJwtInformationByAccessToken(jwt)) {
                    throw new IllegalArgumentException("Registry에 없는 JWT");
                }

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

                DiscodeitUserDetails userDetails =
                        new DiscodeitUserDetails(dto, null);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // WebSocket Principal 설정
                accessor.setUser(authentication);

                // SecurityContext에도 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("WebSocket JWT 인증 성공: user_id={}, email={}, role={}",
                        userId, email, role);

            } catch (Exception e) {
                log.error("WebSocket JWT 인증 실패: {}", e.getMessage());
                throw new AccessDeniedException("JWT 인증 실패");
            }
        }

        return message;
    }
}
