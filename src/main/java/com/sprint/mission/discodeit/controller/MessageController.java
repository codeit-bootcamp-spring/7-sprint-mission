package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.global.dto.ApiResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;

    // 메시지 생성
    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public ResponseEntity<Message> createMessage(
            @RequestPart("messageCreateRequest") CreateMessageRequestDto requestDto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        List<CreateBinaryContentRequestDto> attachmentRequests;
        if(attachments != null || !attachments.isEmpty()) {
            attachmentRequests = attachments.stream()
                .map(attachment -> {
                        try {
                            return new CreateBinaryContentRequestDto(
                                    attachment.getOriginalFilename(),
                                    attachment.getContentType(),
                                    attachment.getBytes());
                        } catch(IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                ).collect(Collectors.toList());
        } else {
            attachmentRequests = new ArrayList<>();
        }

        Message createdMessage = messageService.create(requestDto, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    // 메시지 수정
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<Message> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody UpdateMessageRequestDto requestDto
    ) {
        Message updatedMessage = messageService.update(messageId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    // 메시지 삭제
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 특정 채널 메시지 목록 조회
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> searchMessage(@RequestParam UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(messageList);
    }
}
