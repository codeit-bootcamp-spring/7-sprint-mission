package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageSendCommand;
import com.sprint.mission.discodeit.dto.message.MessageSendRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/messages")
    public void handleMessage(@Payload MessageSendRequestDto messageSendRequestDto) {
        MessageSendCommand command = MessageSendCommand.from(messageSendRequestDto, null);
        messageService.sendMessageToChannel(command);
    }
}
