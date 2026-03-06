package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageWebSocketController {
    private final MessageService messageService;

    @MessageMapping("/messages")
    public void sendMessage(@Valid CreateMessageDto createMessageDto) {
        log.info("WebSocket message received {}", createMessageDto);
        messageService.createMessage(createMessageDto, List.of());
        log.debug("message Send {}", createMessageDto.content());
    }
}
