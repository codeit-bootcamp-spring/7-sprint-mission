package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.service.BasicMessageService;
import com.sprint.mission.discodeit.service.dto.request.MessageForm;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.service.dto.response.MessageResponse;
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
public class MessageController implements MessageControllerDocs {

    private final BasicMessageService messageService;


    @PostMapping
    public MessageResponse sendMessage(@RequestPart("messageCreateRequest") MessageForm messageCreateRequest,
                                       @RequestPart(required = false) List<MultipartFile> attachments) {
        return messageService.sendMessage(messageCreateRequest, attachments);
    }

    @PatchMapping("/{messageId}")
    public MessageResponse updateMessage(@PathVariable UUID messageId, @ModelAttribute MessageUpdate messageUpdate) {
        return messageService.updateMessage(messageId, messageUpdate);
    }

    @DeleteMapping("/{messageId}")
    public String deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return "삭제 성공";
    }

    @GetMapping
    public List<MessageResponse> getAllMessageByChannelId(@RequestParam UUID channelId) {
        return messageService.getAllMessage(channelId);
    }


}
