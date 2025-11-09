package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageRemoveRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody MessageSendRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(request));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> edit(@Valid @RequestBody MessageEditRequest request) {
        return ResponseEntity.ok(messageService.editMessage(request));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public ResponseEntity<Void> remove(@Valid @RequestBody MessageRemoveRequest request) {
        messageService.delete(request);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<List<MessageResponse>> get(@Valid @RequestParam MessageGetRequest request) {
        return ResponseEntity.ok(messageService.get(request));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponse>> getAll() {
        return ResponseEntity.ok(messageService.getAll());
    }
}
