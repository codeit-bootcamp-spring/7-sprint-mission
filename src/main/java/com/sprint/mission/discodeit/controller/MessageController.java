package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
//    메시지를 수정할 수 있다.
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable UUID id, @RequestBody UpdateMessageDto request){
        MessageResponseDto response = messageService.updateMessage(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    메시지를 삭제할 수 있다.
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //특정 채널의 메시지 목록을 조회할 수 있다.
    // api/messages?channelId=..
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponseDto>> findChannelByMessages(@RequestParam UUID channelId){
        List<MessageResponseDto> response = messageService.findAllByChannelId(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
