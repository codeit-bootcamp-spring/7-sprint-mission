package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    // 이미지 첨부 메시지 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto create(
            @RequestPart("json") MessageCreateRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        return messageService.createWithFile(request, file);
    }


    // 메시지 수정
    @PutMapping("/{id}")
    public MessageDto update(
            @PathVariable UUID id,
            @RequestBody MessageUpdateRequest request
    ) {
        MessageUpdateRequest fixed = new MessageUpdateRequest(id, request.content());
        return messageService.update(fixed);
    }

    // 메시지 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        messageService.delete(id);
    }

    // 메시지 조회 (페이징)
    @GetMapping("/channel/{channelId}")
    public PageResponse<MessageDto> findAllByChannelId(
            @PathVariable UUID channelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return messageService.findAllByChannelId(channelId, page, size);
    }

    // 메시지 단건 조회
    @GetMapping("/{id}")
    public MessageDto find(@PathVariable UUID id) {
        return messageService.find(id);
    }
}
