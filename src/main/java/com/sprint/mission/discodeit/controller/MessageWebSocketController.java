package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService  messageService;

    @MessageMapping("/pub/messages")
    public ResponseEntity<MessageDto> sendOnlyTestMessage(@DestinationVariable String channelId,
                                                          @Payload MessageCreateRequestDto request) {
        log.info("채널 - 메세지 생성, 채널 id - {}", channelId);
        MessageDto message = messageService.createMessage(request, null);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
