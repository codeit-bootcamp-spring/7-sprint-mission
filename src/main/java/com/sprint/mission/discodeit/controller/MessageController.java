package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.messageDto.ChannelMessageRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageResponseDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // --- 메시지 관리 ---
    // (채널)메시지 생성
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponseDto> channelMessageCreate(@RequestBody ChannelMessageRequestDto channelMessageRequestDto) {
        MessageResponseDto dto = messageService.createChannelMessage(channelMessageRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // 메시지 수정
    // 비어있다면 삭제. 라는 기능을 넣고 싶어서 이렇게 했는데... 맞는건가
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<MessageResponseDto> channelMessageUpdate(@RequestBody MessageUpdateDto updateDto) {
        Optional<MessageResponseDto> messageOp = messageService.updateMessage(updateDto);
        return messageOp.map(messageResponseDto
                        -> ResponseEntity.status(HttpStatus.OK).body(messageResponseDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    // 메시지 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> channelMessageDelete(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    // 특정 채널의 메시지 조회
    @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponseDto>> getAllByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    // 테스트용 모든메시지 조회
//    @RequestMapping(method = RequestMethod.GET)
//    public List<MessageResponseDto> getAll() {
//        return messageService.findAll();
//    }
}

/*
메시지 관리
[ ] 메시지를 보낼 수 있다.
[ ] 메시지를 수정할 수 있다.
[ ] 메시지를 삭제할 수 있다.
[ ] 특정 채널의 메시지 목록을 조회할 수 있다.

바이너리 파일 다운로드
[ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.
 */