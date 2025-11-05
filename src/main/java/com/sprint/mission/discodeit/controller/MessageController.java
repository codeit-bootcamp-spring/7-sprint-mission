package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    // 메시지를 보낼 수 있다.
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody CreateMessageRequestDto request) {
        MessageResponseDto message = messageService.createMessage(request);
        return ResponseEntity.ok(message);
    }
//    메시지를 수정할 수 있다.
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable UUID id, @RequestBody UpdateMessageDto request){
        MessageResponseDto response = messageService.updateMessage(id, request);
        return ResponseEntity.ok(response);
    }
//    메시지를 삭제할 수 있다.
//    특정 채널의 메시지 목록을 조회할 수 있다.
//    특정 채널의 메시지 수신 정보를 생성할 수 있다.
//    특정 채널의 메시지 수신 정보를 수정할 수 있다.
//    특정 사용자의 메시지 수신 정보를 조회할 수 있다.
//    바이너리 파일을 1개 또는 여러 개 조회할 수 있다.



}
