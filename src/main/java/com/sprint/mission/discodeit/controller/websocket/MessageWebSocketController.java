package com.sprint.mission.discodeit.controller.websocket;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/messages")
    public void sendMessage(CreateMessageRequestDto request) {
        messageService.create(request, null);
    }
}
