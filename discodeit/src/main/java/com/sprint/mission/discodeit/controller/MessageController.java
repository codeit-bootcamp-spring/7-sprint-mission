package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // ✅ 1. 메시지를 보낼 수 있다
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<com.sprint.mission.discodeit.dto.data.MessageDto> sendMessage(
            @RequestBody MessageCreateRequest messageRequest
    ) {
        // 첨부파일 없을 경우 빈 리스트 전달
        List<BinaryContentCreateRequest> attachments = List.of();

        com.sprint.mission.discodeit.entity.Message created =
                messageService.create(messageRequest, attachments);

        return ResponseEntity.ok(
                com.sprint.mission.discodeit.dto.data.MessageDto.from(created)
        );
    }

    // ✅ 2. 메시지를 수정할 수 있다
    @RequestMapping(method = RequestMethod.PUT, value = "/{messageId}")
    public ResponseEntity<com.sprint.mission.discodeit.dto.data.MessageDto> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest request
    ) {
        com.sprint.mission.discodeit.entity.Message updated =
                messageService.update(messageId, request);

        return ResponseEntity.ok(
                com.sprint.mission.discodeit.dto.data.MessageDto.from(updated)
        );
    }

    // ✅ 3. 메시지를 삭제할 수 있다
    @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    // ✅ 4. 특정 채널의 메시지 목록을 조회할 수 있다
    @RequestMapping(method = RequestMethod.GET, value = "/channel/{channelId}")
    public ResponseEntity<List<com.sprint.mission.discodeit.dto.data.MessageDto>> getChannelMessages(
            @PathVariable UUID channelId
    ) {
        List<com.sprint.mission.discodeit.dto.data.MessageDto> messages = messageService.findAllByChannelId(channelId)
                .stream()
                .map(com.sprint.mission.discodeit.dto.data.MessageDto::from)
                .toList();

        return ResponseEntity.ok(messages);
    }
}
// object 반환??? ㅜ