package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.global.util.ApiResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * [ ] 메시지를 보낼 수 있다.  /api/messages, POST
 * [ ] 메시지를 수정할 수 있다. /api/messages/{messageId}, PUT
 * [ ] 메시지를 삭제할 수 있다. /api/messages/{messageId}, DELETE
 * [ ] 특정 채널의 메시지 목록을 조회할 수 있다. /api/messages?channelId=, GET
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<MessageResponseDto>> createMessage(
            @Valid @RequestPart CreateMessageDto createMessageDto,
            @RequestPart(value = "attachments") List<MultipartFile> attachments) {
        List<CreateBinaryContentDto> createBinaryContentDtos = attachments.stream()
                .map(attachment -> {
                    CreateBinaryContentDto createBinaryContentDto = null;
                    try {
                        createBinaryContentDto = new CreateBinaryContentDto(
                                attachment.getOriginalFilename(),
                                attachment.getContentType(),
                                attachment.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return createBinaryContentDto;
                }).toList();

        MessageResponseDto messageResponseDto = messageService.createMessage(createMessageDto, createBinaryContentDtos);
        ApiResponse<MessageResponseDto> responseBody = ApiResponse.success(messageResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<MessageResponseDto>> updateMessage(@PathVariable UUID messageId, @RequestBody UpdateMessageDto updateMessageDto) {
        MessageResponseDto messageResponseDto = messageService.updateMessage(messageId, updateMessageDto);
        ApiResponse<MessageResponseDto> responseBody = ApiResponse.success(messageResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<MessageResponseDto>>> getMessages(@RequestParam UUID channelId) {
        List<MessageResponseDto> allMessageByChannelId = messageService.getAllMessageByChannelId(channelId);
        ApiResponse<List<MessageResponseDto>> responseBody = ApiResponse.success(allMessageByChannelId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


}
