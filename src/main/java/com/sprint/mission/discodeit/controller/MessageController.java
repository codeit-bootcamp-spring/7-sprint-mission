package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // --- 메시지 관리 ---
    // (채널)메시지 생성
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> channelMessageCreate(
            @Valid @RequestPart("MessageRequestDto") MessageRequestDto messageRequestDto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.createMessage(messageRequestDto, attachments));
    }

    // 메시지 수정
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<Message> channelMessageUpdate(
            @PathVariable UUID messageId,
            @Valid @RequestBody MessageUpdateDto updateDto) {

        return ResponseEntity.ok(messageService.updateMessage(messageId, updateDto));
    }

    // 메시지 삭제
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> channelMessageDelete(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    // 특정 채널의 메시지 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getAllByChannelId(@RequestParam UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}

/*
메시지 관리
[ ] 메시지를 보낼 수 있다.
[ ] 메시지를 수정할 수 있다.
[ ] 메시지를 삭제할 수 있다.
[ ] 특정 채널의 메시지 목록을 조회할 수 있다.

바이너리 파일 다운로드
[ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.
 */