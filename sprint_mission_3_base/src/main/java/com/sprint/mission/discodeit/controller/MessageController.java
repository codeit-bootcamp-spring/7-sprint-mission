package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // ✅ 메시지 생성
    @PostMapping
    public ResponseEntity<MessageDto> create(@RequestBody MessageCreateRequest request) {
        MessageDto created = messageService.create(request);
        return ResponseEntity.status(201).body(created);
    }

    // ✅ 채널별 메시지 조회
    // 기존: /channel/{channelId} → 개선: /api/messages?channelId={id}
    @GetMapping
    public ResponseEntity<List<MessageDto>> findAllByChannel(@RequestParam UUID channelId) {
        List<MessageDto> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }

    // ✅ 메시지 내용 수정
    @PatchMapping
    public ResponseEntity<MessageDto> update(@RequestBody MessageUpdateRequest request) {
        MessageDto updated = messageService.update(request);
        return ResponseEntity.ok(updated);
    }

    // ✅ 메시지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
