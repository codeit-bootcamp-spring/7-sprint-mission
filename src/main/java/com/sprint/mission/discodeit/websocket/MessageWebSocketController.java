package com.sprint.mission.discodeit.websocket;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/messages")
    public void sendMessage(MessageCreateRequest request) {
        messageService.sendMessage(request, null);
    }
}
