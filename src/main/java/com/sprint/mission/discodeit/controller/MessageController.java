package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicChannelService;
import com.sprint.mission.discodeit.application.BasicMessageService;
import com.sprint.mission.discodeit.application.dto.request.Attachments;
import com.sprint.mission.discodeit.application.dto.request.MessageForm;
import com.sprint.mission.discodeit.application.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.application.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final BasicMessageService messageService;



    @PostMapping
    public MessageResponse sendMessage(@ModelAttribute MessageForm messageCreateRequest,
                                       @ModelAttribute Attachments attachments) {
        return messageService.sendMessage(messageCreateRequest, attachments);
    }

    @PatchMapping("/{messageId}")
    public MessageResponse updateMessage(@PathVariable UUID messageId, @ModelAttribute MessageUpdate messageUpdate) throws IOException {
         return messageService.updateMessage(messageId, messageUpdate);
    }
    @DeleteMapping( "/{messageId}")
    public String deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return "삭제 성공";
    }

    @GetMapping("/{messageId}")
    public List<UUID> getMessageImageId(@PathVariable UUID messageId){
        return messageService.getMessageImageId(messageId);
    }

    @GetMapping
    public List<MessageResponse> getAllMessageByChannelId(@RequestParam UUID channelId){
        return messageService.getAllMessage(channelId);
    }
}
