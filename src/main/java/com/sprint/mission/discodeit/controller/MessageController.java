package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicMessageService;
import com.sprint.mission.discodeit.application.dto.request.MessageForm;
import com.sprint.mission.discodeit.application.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.application.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final BasicMessageService messageService;


    @PostMapping( "/add")
    public MessageResponse sendMessage(@ModelAttribute MessageForm form) throws IOException {
        return  messageService.sendMessage(form);
    }

    @PostMapping("/edit")
    public MessageResponse updateMessage(@ModelAttribute MessageUpdate messageUpdate) throws IOException {
         return messageService.updateMessage(messageUpdate);
    }
    @DeleteMapping( "/{messageId}")
    public String deleteMessage(@PathVariable UUID messageId) throws IOException {
        log.info("MessageController deleteMessage{}",messageId);
        messageService.deleteMessage(messageId);
        return "삭제 성공";
    }

    @GetMapping("/{messageId}")
    public UUID getMessageImageId(@PathVariable UUID messageId){
        return messageService.getMessageImageId(messageId);
    }
}
