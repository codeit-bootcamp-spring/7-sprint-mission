package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BasicMessageService;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final BasicMessageService messageService;


    @PostMapping
    public MessageDto sendMessage(@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                  @RequestPart(required = false) List<MultipartFile> attachments) {
        return messageService.sendMessage(messageCreateRequest, attachments);
    }

    @PatchMapping("/{messageId}")
    public MessageDto updateMessage(@PathVariable String messageId, @ModelAttribute MessageUpdate messageUpdate) {
        return messageService.updateMessage(messageId, messageUpdate);
    }

    @DeleteMapping("/{messageId}")
    public String deleteMessage(@PathVariable String messageId) {
        messageService.deleteMessage(messageId);
        return "삭제 성공";
    }

    @GetMapping
    public List<MessageDto> getAllMessageByChannelId(@RequestParam String channelId) {
        return messageService.getAllMessage(channelId);
    }


}
