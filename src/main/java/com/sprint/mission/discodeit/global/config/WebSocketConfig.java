package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.config.security.interceptor.AuthChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptor authChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 구독용 Prefix 설정 (메시지 브로커)
        config.enableSimpleBroker("/sub");
        // 발행용 Prefix 설정 (애플리케이션으로 요청 보내는 용도)
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:9090", "http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(
                authChannelInterceptor,
                new SecurityContextChannelInterceptor()
        );
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration
                .setMessageSizeLimit(128 * 1024)        // 메시지 크기 제한: 128KB
                .setSendBufferSizeLimit(512 * 1024)     // 전송 버퍼 크기: 512KB
                .setSendTimeLimit(20 * 1000)            // 전송 타임아웃: 20초
                .setTimeToFirstMessage(30 * 1000);      // 첫 메시지 타임아웃: 30초
    }
}
