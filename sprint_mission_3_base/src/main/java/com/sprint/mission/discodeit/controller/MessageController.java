package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.message.*;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/messages")
public class MessageController {


    private final MessageService messageService;


    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @RequestMapping(method = RequestMethod.POST)
    public MessageDto create(@RequestBody MessageCreateRequest request) {
        return messageService.create(request);
    }


    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public List<MessageDto> findAllByChannel(@PathVariable UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }


    @RequestMapping(method = RequestMethod.PUT)
    public MessageDto update(@RequestBody MessageUpdateRequest request) {
        return messageService.update(request);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        messageService.delete(id);
    }
}