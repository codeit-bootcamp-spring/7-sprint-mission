package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.facade.message.MessageCreationFacade;
import com.sprint.mission.discodeit.facade.message.MessageDeleteFacade;
import com.sprint.mission.discodeit.facade.message.MessageOverviewFacade;
import com.sprint.mission.discodeit.facade.message.MessageUpdateFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageCreationFacade messageCreationFacade;
    private final MessageOverviewFacade messageOverviewFacade;
    private final MessageUpdateFacade messageUpdateFacade;
    private final MessageDeleteFacade messageDeleteFacade;

    //특정 채널의 메세지들 조회
    @RequestMapping(method= RequestMethod.GET, value = "/{channelId}/list")
    public ResponseEntity<List<MessageViewRes>> findAllByChannelId(@PathVariable UUID channelId){
        return ResponseEntity.ok(messageOverviewFacade.findAllByChannelId(channelId));
    }

    //메세지 입력
    @RequestMapping(method=RequestMethod.POST, value = "/{channelId}")
    public ResponseEntity<Void> createMessage(@RequestHeader("X-LOGINUSER-ID") UUID speakerId,
                                              @PathVariable UUID channelId,
                                              @RequestBody MessageCreateReq req){
        Message message =  messageCreationFacade.createMessage(speakerId, channelId, req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(message.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    //메세지 수정
    @RequestMapping(method=RequestMethod.PUT, value="/{messageId}")
    public ResponseEntity<Void> updateMessage(@PathVariable UUID messageId,
                                              @RequestBody MessageUpdateReq req){
        messageUpdateFacade.updateMessage(messageId, req);
        return ResponseEntity.noContent().build();
    }

    //메세지 삭제
    @RequestMapping(method=RequestMethod.DELETE, value="/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId){
        messageDeleteFacade.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
