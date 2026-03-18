package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/message")
    public void createMessage(MessageCreateRequest request) {
        log.info("웹소켓 메시지 - authorId={}, channelId={}", request.getAuthorId(), request.getChannelId());

        messageService.createMessage(request, null);
    }
}
