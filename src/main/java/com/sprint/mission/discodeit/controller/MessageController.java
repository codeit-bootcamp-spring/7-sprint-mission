package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/messages")
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public MessageResponseDto create(@Valid @RequestBody MessageCreateRequestDto messageCreateRequestDto) {
        Message message = messageService.create(messageCreateRequestDto);
        return MessageResponseDto.from(message);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MessageResponseDto get(@PathVariable("id") UUID id) {
        Message message = messageService.get(id);
        return MessageResponseDto.from(message);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MessageResponseDto update(@PathVariable("id") UUID id,
                                     @Valid @RequestBody MessageUpdateRequestDto messageUpdateRequestDto) {
        if(messageUpdateRequestDto.messageId() == null
        || !id.equals(messageUpdateRequestDto.messageId())) {
            throw new IllegalArgumentException("Invalid message id");
        }
        Message message = messageService.update(messageUpdateRequestDto);
        return MessageResponseDto.from(message);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        messageService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MessageResponseDto> getByChannelId(
            @RequestParam("channelId") UUID channelId,
            @RequestParam("userId") UUID userId)  {
        List<Message> messages = messageService.getAllByChannelForUser(channelId, userId);
        return messages.stream()
                .map(message -> MessageResponseDto.from(message))
                .toList();
    }
}
