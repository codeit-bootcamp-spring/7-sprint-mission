package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> createMessage(@RequestPart("request") CreateMessageRequestDto requestDto,
                                                 @RequestPart(value = "file", required = false) List<MultipartFile> file) {
        messageService.create(requestDto, file);
        return ResponseEntity.ok().body("message created");
    }

    // 메시지 수정
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateMessage(@PathVariable UUID messageId,
                                                @RequestBody UpdateMessageRequestDto requestDto) {
        messageService.update(messageId, requestDto);
        return ResponseEntity.ok().body("message updated");
    }

    // 메시지 삭제
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok().body("message deleted");
    }

    // 특정 채널 메시지 목록 조회
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> searchMessage(@RequestParam UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok().body(messageList);
    }

}
