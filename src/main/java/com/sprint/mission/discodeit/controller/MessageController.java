package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;


    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                                  @RequestPart(required = false) List<MultipartFile> attachments) {

        MessageDto messageDto = messageService.sendMessage(messageCreateRequest, attachments);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(
            @PathVariable UUID messageId,
            @ModelAttribute MessageUpdateRequest messageUpdateRequest) {
        MessageDto messageDto = messageService.updateMessage(messageId, messageUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<MessageDto>> getAllMessageByChannelId(
            @RequestParam(value = "channelId") UUID channelId,
            @RequestParam(value="cursor", required = false) Instant cursor,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort) {
        PageResponse<MessageDto> allByChannelId = messageService.getAllByChannelId(channelId, cursor, size, sort);

        return ResponseEntity.status(HttpStatus.OK).body(allByChannelId);
    }
}
