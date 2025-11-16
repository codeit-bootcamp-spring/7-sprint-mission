package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.MessageGetByChannelIdRequest;
import com.sprint.mission.discodeit.dto.message.request.*;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseV2;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponseV2>> getAll() {
        return ResponseEntity.ok(messageService.getAll());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponseV2>> getByChannelId(@Valid @RequestParam UUID channelId) {
        return ResponseEntity.ok(messageService.getAllByChannelId(new MessageGetByChannelIdRequest(channelId)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponseV2> send(@Valid @RequestPart(name = "messageCreateRequest") MessageCreateRequestV2 messageCreateRequest, @RequestPart(name = "attachments", required = false) List<MultipartFile> attachments) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(messageCreateRequest, attachments));
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> remove(@RequestParam UUID messageId) {
        messageService.remove(messageId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<MessageResponseV2> edit(@RequestParam UUID messageId, @Valid @RequestBody MessageEditRequest request) {
        return ResponseEntity.ok(messageService.editMessage(messageId, request));
    }
}
