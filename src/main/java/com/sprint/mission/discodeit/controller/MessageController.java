package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/messages")
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto create(@Valid @RequestBody MessageCreateRequestDto messageCreateRequestDto,
                                     @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        return messageService.create(messageCreateRequestDto, attachments);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
    public MessageResponseDto get(@PathVariable("messageId") UUID messageId) {
        return messageService.get(messageId);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MessageResponseDto update(@PathVariable("messageId") UUID messageId,
                                     @Valid @RequestBody MessageUpdateRequestDto messageUpdateRequestDto) {
        return messageService.update(messageId, messageUpdateRequestDto);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("messageId") UUID messageId) {
        messageService.delete(messageId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MessageResponseDto> getByChannelId(
            @RequestParam("channelId") UUID channelId)  {
        return messageService.getAllByChannelId(channelId);
    }
}
