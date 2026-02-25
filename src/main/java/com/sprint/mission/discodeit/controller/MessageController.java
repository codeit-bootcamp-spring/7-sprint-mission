package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.MessageApi;
import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Override
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<MessageUpdateResponseDto> updateMessage(@PathVariable UUID messageId, @Valid @RequestBody MessageUpdateRequestDto request) {
        log.debug("메시지 수정 요청 - messageId={}", messageId);
        MessageUpdateCommand command = MessageUpdateCommand.from(request, messageId);
        messageService.updateMessage(command);
        log.info("메시지 수정 성공 - messageId={}", messageId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        log.debug("메시지 삭제 요청 - messageId={}", messageId);
        messageService.deleteMessage(messageId);
        log.info("메시지 삭제 성공 - messageId={}", messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @Timed("message.create.async")
    @PostMapping(value = "/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponseDto> sendMessageByChannelId(
            @Valid @RequestPart("messageCreateRequest") MessageSendRequestDto request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> files
    ) {
        log.info(
                "메시지 전송 요청 - channelId={}, senderId={}, attachments={}",
                request.channelId(),
                request.authorId(),
                files == null ? 0 : files.size()
        );
        MessageSendCommand messageSendCommand = MessageSendCommand.from(request, files);
        MessageResponseDto responseDto = messageService.sendMessageToChannel(messageSendCommand);
        log.info(
                "메시지 전송 성공 - messageId={}, channelId={}",
                responseDto.id(),
                responseDto.channelId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    @GetMapping("/messages")
    public ResponseEntity<PageResponse<MessageResponseDto>> getAllMessagesByChannelId(
            @RequestParam UUID channelId,
            Pageable pageable, // Pageable 명시에 놓으면 PageableHandlerMethodArgumentResolver 를통해 프론트에서 파라미터 형식 이름만 맞추면 바인딩해줌
            @RequestParam(required = false) Instant cursor
            ) {
        log.debug(
                "메시지 목록 조회 요청 - channelId={}, page={}, size={}, cursor={}",
                channelId,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                cursor
        );
        PageResponse<MessageResponseDto> allMessagesByChannelId = messageService.getAllMessagesByChannelId(channelId, pageable, cursor);

        log.debug(
                "메시지 목록 조회 성공 - channelId={}, count={}",
                channelId,
                allMessagesByChannelId.content().size()
        );
        return ResponseEntity.ok(allMessagesByChannelId);
    }
}
