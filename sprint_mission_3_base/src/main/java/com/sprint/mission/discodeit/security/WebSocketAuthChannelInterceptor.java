package com.sprint.mission.discodeit.security;

import com.nimbusds.jwt.JWTClaimsSet;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String authorization = accessor.getFirstNativeHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return message;
        }

        String token = authorization.substring(7);
        if (!jwtTokenProvider.validate(token)) {
            return message;
        }

        JWTClaimsSet claims = jwtTokenProvider.parseClaims(token);
        Object type = claims.getClaim(JwtTokenProvider.CLAIM_TOKEN_TYPE);
        if (!JwtTokenProvider.TYPE_ACCESS.equals(type)) {
            return message;
        }

        String email = claims.getSubject();
        Principal principal = (Principal) userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        ((DiscodeitUserDetails) principal).getAuthorities()
                );

        accessor.setUser(authentication);
        return message;
    }
}