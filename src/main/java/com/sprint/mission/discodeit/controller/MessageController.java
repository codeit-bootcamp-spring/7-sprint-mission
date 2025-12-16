package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.Response.PageResponseDto;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageControllerDocs {
    private final MessageService messageService;

    private final BinaryContentMapper binaryContentMapper;

    // 메시지 생성
    @PostMapping
    public ResponseEntity<MessageResponseDto> createMessage(
            @RequestPart("messageCreateRequest") CreateMessageRequestDto requestDto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        log.info("POST /api/messages - 메시지 생성 요청");

        List<CreateBinaryContentRequestDto> attachmentRequests = binaryContentMapper.toRequestDto(attachments);
        MessageResponseDto createdMessage = messageService.create(requestDto, attachmentRequests);

        log.info("POST /api/messages - 메시지 생성 완료: messageId = {}", createdMessage.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    // 메시지 수정
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponseDto> updateMessage(
            @PathVariable UUID messageId,
            @Valid @RequestBody UpdateMessageRequestDto requestDto
    ) {
        log.info("PATCH /api/messages/{} - 메시지 수정 요청", messageId);

        MessageResponseDto updatedMessage = messageService.update(messageId, requestDto);

        log.info("PATCH /api/messages/{} - 메시지 수정 완료", messageId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    // 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        log.info("DELETE /api/messages/{} - 메시지 삭제 요청", messageId);

        messageService.delete(messageId);

        log.info("DELETE /api/messages/{} - 메시지 삭제 완료", messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 특정 채널 메시지 목록 조회
    @GetMapping
    public ResponseEntity<PageResponseDto<MessageResponseDto>> searchMessage(
            @RequestParam UUID channelId,
            @RequestParam(required = false) Instant cursor,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        PageResponseDto<MessageResponseDto> response = messageService.findAllByChannelId(channelId, cursor, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
