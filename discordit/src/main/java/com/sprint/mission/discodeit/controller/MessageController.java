package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.channel.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.entity.message.MessageDto;
import com.sprint.mission.discodeit.dto.entity.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.entity.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<PageResponse<?>> getByChannelId(@Valid @RequestParam UUID channelId, Pageable pageable) {
        return ResponseEntity.ok(messageService.getAllByChannelId(new MessageGetRequest(channelId, pageable)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> send(@Valid @RequestPart(name = "messageCreateRequest") MessageCreateRequest messageCreateRequest, @RequestPart(name = "attachments", required = false) List<MultipartFile> attachments) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(messageCreateRequest, attachments));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> remove(@RequestParam UUID messageId) {
        messageService.remove(messageId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> edit(@RequestParam UUID messageId, @Valid @RequestBody MessageEditRequest request) {
        return ResponseEntity.ok(messageService.editMessage(messageId, request));
    }
}
