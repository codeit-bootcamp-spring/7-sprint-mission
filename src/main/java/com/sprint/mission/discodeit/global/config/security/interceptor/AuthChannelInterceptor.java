package com.sprint.mission.discodeit.global.config.security.interceptor;

import com.sprint.mission.discodeit.global.config.security.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        log.debug("AuthChannelInterceptor{}\n{}\n{}", message, channel, accessor);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // STOMP CONNECT 프레임에서 토큰 추출
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                if (jwtProvider.validateAccessToken(token)) {
                    String username = jwtProvider.extractSubject(token);
                    accessor.setUser(new UsernamePasswordAuthenticationToken(
                            username, null, Collections.emptyList()
                    ));
                } else {
                    throw new IllegalArgumentException("AuthChannelInterceptor, 유효하지 않은 토큰입니다.");
                }
            }
        }

        return message;
    }
}