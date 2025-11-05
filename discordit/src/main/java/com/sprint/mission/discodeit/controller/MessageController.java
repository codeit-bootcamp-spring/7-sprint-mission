package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageDeleteRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> send(MessageSendRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(request));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> edit(MessageEditRequest request) {
        return ResponseEntity.ok(messageService.editMessage(request));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(MessageDeleteRequest request) {
        messageService.delete(request);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<MessageResponse>> get(MessageGetRequest request) {
        return ResponseEntity.ok(messageService.get(request));
    }
}
