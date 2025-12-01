package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.page.PageResponseDto;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/messages")
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto create(
            @Valid @RequestPart("messageCreateRequest") MessageCreateRequestDto messageCreateRequestDto,
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

    /*
    @RequestMapping(method = RequestMethod.GET)
    public List<MessageResponseDto> getByChannelId(
            @RequestParam("channelId") UUID channelId)  {
        return messageService.getAllByChannelId(channelId);
    }

     */


    @RequestMapping(method = RequestMethod.GET)
    public PageResponseDto<MessageResponseDto> getMessages(
            @RequestParam("channelId") UUID channelId,
            @PageableDefault(
                    size = 50,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return messageService.getPageByChannelId(channelId, pageable);
    }
}
