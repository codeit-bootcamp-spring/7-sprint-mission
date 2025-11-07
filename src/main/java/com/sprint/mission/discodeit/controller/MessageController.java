package com.sprint.mission.discodeit.controller;

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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;

    // 메시지 생성
    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Object>> createMessage(@RequestPart("request") CreateMessageRequestDto requestDto,
                                                 @RequestPart(value = "file", required = false) List<MultipartFile> file) {
        messageService.create(requestDto, file);
        return ApiResponse.success(HttpStatus.CREATED,"메시지가 생성되었습니다.");
    }

    // 메시지 수정
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<Object>> updateMessage(@PathVariable UUID messageId,
                                                @RequestBody UpdateMessageRequestDto requestDto) {
        messageService.update(messageId, requestDto);
        return ApiResponse.success(HttpStatus.OK,"메시지가 수정되었습니다.");
    }

    // 메시지 삭제
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<Object>> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ApiResponse.success(HttpStatus.OK,"메시지가 삭제되었습니다.");
    }

    // 특정 채널 메시지 목록 조회
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<Message>>> searchMessage(@RequestParam UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        return ApiResponse.success(HttpStatus.OK,"채널 메시지 목록 조회", messageList);
    }

}
