package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;


    @PostMapping
    public MessageDto sendMessage(@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                  @RequestPart(required = false) List<MultipartFile> attachments) {
        return messageService.sendMessage(messageCreateRequest, attachments);
    }

    @PatchMapping("/{messageId}")
    public MessageDto updateMessage(@PathVariable UUID messageId, @ModelAttribute MessageUpdateRequest messageUpdateRequest) {
        return messageService.updateMessage(messageId, messageUpdateRequest);
    }

    @DeleteMapping("/{messageId}")
    public String deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return "삭제 성공";
    }

    @GetMapping
    public List<MessageDto> getAllMessageByChannelId(@RequestParam UUID channelId) {
        return messageService.getAllByChannelId(channelId);
    }


}
