package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ChannelService channelService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> createMessage(@Valid @RequestPart("messageCreateRequest") MessageCreateRequestDto dto,
                                                    @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) throws IOException {
        return new ResponseEntity<>(messageService.createMessage(dto, attachments), HttpStatus.CREATED);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> patchMessage(@PathVariable UUID messageId, @Valid @RequestBody MessagePatchRequestDto dto){
        MessageDto mRRDto = messageService.patchMessage(dto, messageId);
        return new ResponseEntity<>(mRRDto, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable UUID messageId){
        messageService.deleteMessage(messageId);
    }

    @GetMapping("")
    public ResponseEntity<PageResponseDto<MessageDto>> readChannelMessage(
            @RequestParam UUID channelId, @RequestParam (required = false)Pageable pageable
        , @RequestParam (required = false)String cursor
    ){
        return new ResponseEntity<>( messageService.findallByChannelIdWithCursor(channelId,cursor,pageable), HttpStatus.OK);
    }
    @PostMapping( "/reset")
    public void reset(){
        messageService.resetMessage();
    }

    @GetMapping("/all")
    public ResponseEntity<List<MessageDto>> readAll(){
        return new ResponseEntity<>(messageService.readAllMessage(), HttpStatus.OK);
    }

}
