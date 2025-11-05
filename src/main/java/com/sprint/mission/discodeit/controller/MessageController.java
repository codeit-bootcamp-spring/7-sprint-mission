package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * [ ] 메시지를 보낼 수 있다.  /api/message, POST
 * [ ] 메시지를 수정할 수 있다. /api/message/{messageId}, PUT
 * [ ] 메시지를 삭제할 수 있다. /api/message/{messageId}, DELETE
 * [ ] 특정 채널의 메시지 목록을 조회할 수 있다. /api/message?channelId=, GET
 */
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createMessage(@RequestBody CreateMessageDto createMessageDto) {
        MessageResponseDto message = messageService.createMessage(createMessageDto);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMessage(@PathVariable UUID messageId, @RequestBody UpdateMessageDto updateMessageDto) {
        MessageResponseDto messageResponseDto = messageService.updateMessage(messageId, updateMessageDto);
        return ResponseEntity.ok(messageResponseDto);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getMessages(@RequestParam UUID channelId) {
        List<MessageResponseDto> allMessageByChannelId = messageService.getAllMessageByChannelId(channelId);
        return ResponseEntity.ok(allMessageByChannelId);
    }


}
