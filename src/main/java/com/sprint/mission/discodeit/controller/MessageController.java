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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageControllerDocs {
    private final MessageService messageService;

    private final BinaryContentMapper binaryContentMapper;
    private final PageMapper pageMapper;

    // 메시지 생성
    @PostMapping
    public ResponseEntity<MessageResponseDto> createMessage(
            @RequestPart("messageCreateRequest") CreateMessageRequestDto requestDto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        List<CreateBinaryContentRequestDto> attachmentRequests = binaryContentMapper.toRequestDto(attachments);
        MessageResponseDto createdMessage = messageService.create(requestDto, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    // 메시지 수정
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponseDto> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody UpdateMessageRequestDto requestDto
    ) {
        MessageResponseDto updatedMessage = messageService.update(messageId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    // 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 특정 채널 메시지 목록 조회
    @GetMapping
    public ResponseEntity<PageResponseDto<MessageResponseDto>> searchMessage(
            @RequestParam UUID channelId,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        Page<MessageResponseDto> messageList = messageService.findAllByChannelId(channelId, pageable);
        PageResponseDto<MessageResponseDto> messageResponse = pageMapper.toResponseDto(messageList);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
}
