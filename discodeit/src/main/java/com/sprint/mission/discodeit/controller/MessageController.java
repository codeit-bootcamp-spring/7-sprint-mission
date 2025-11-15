package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // ✅ 1. 메시지를 보낼 수 있다
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<MessageDto> sendMessage(
        @RequestPart("message") MessageCreateRequest messageRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) throws IOException {
        // 첨부파일 없을 경우 빈 리스트 전달
        List<BinaryContentCreateRequest> binaryContents = Optional.ofNullable(attachments)
            .orElse(List.of())
            .stream()
            .map(file -> {
                try {
                    return new BinaryContentCreateRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                    );
                } catch (IOException e) {
                    throw new RuntimeException("파일 변환 실패: " + file.getOriginalFilename(), e);
                }
            })
            .collect(Collectors.toList());

        Message created =
                messageService.create(messageRequest, binaryContents);

        return ResponseEntity.status(HttpStatus.CREATED).body(MessageDto.from(created));
    }

    // ✅ 2. 메시지를 수정할 수 있다
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest request
    ) {
        Message updated =
                messageService.update(messageId, request);

        return ResponseEntity.ok(MessageDto.from(updated)
        );
    }

    // ✅ 3. 메시지를 삭제할 수 있다
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    // ✅ 4. 특정 채널의 메시지 목록을 조회할 수 있다
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageDto>> getChannelMessages(
            @PathVariable UUID channelId
    ) {
        List<MessageDto> messages = messageService.findAllByChannelId(channelId)
                .stream()
                .map(MessageDto::from)
                .toList();

        return ResponseEntity.ok(messages);
    }
}