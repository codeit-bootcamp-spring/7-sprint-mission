package com.sprint.mission.discodeit.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class MessageHandShakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String randomMessageId = UUID.randomUUID().toString();
        return new StompPrincipal(randomMessageId);
    }

    private static class StompPrincipal implements Principal {
        private final String randomId;

        private StompPrincipal(String randomId) {
            this.randomId = randomId;
        }

        @Override
        public String getName() {
            return randomId;
        }
    }


}
