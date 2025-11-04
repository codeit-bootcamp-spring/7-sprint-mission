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
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    // 메시지 생성
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createMessage(@RequestPart("request") CreateMessageRequestDto requestDto,
                                                 @RequestPart(value = "file", required = false) List<MultipartFile> file) {
        try {
            messageService.create(requestDto, file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("message created");
    }

    // 메시지 수정
    @RequestMapping(value = "/update/{messageId}", method = RequestMethod.POST)
    public ResponseEntity<String> updateMessage(@PathVariable UUID messageId,
                                                @RequestBody UpdateMessageRequestDto requestDto) {
        try {
            messageService.update(messageId, requestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("message updated");
    }

    // 메시지 삭제
    @RequestMapping(value = "/delete/{messageId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
        try {
            messageService.delete(messageId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("message deleted");
    }

    // 특정 채널 메시지 목록 조회
    @RequestMapping(value = "/search/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> searchMessage(@PathVariable UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok().body(messageList);
    }

}
