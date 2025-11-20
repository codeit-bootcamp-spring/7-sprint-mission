package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageControllerDocs {

    private final MessageService messageService;
    private final ChannelService channelService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<MessageReadResponseDto> createMessage(@Valid @RequestPart("messageCreateRequest") MessageCreateRequestDto dto,
                                                                @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ){

        return new ResponseEntity<>(messageService.createMessage(dto, attachments), HttpStatus.CREATED);
    }

    @PatchMapping("/{messageId}")
    @Override
    public ResponseEntity<MessageReadResponseDto> patchMessage(@PathVariable UUID messageId, @Valid @RequestBody MessagePatchRequestDto dto){
        MessageReadResponseDto mRRDto = messageService.patchMessage(dto, messageId);
        return new ResponseEntity<>(mRRDto, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    @Override
    public void deleteMessage(@PathVariable UUID messageId){
        messageService.deleteMessage(messageId);
    }

    @GetMapping("")
    public ResponseEntity<List<MessageReadResponseDto>> readChannelMessage(@RequestParam UUID channelId){
        return new ResponseEntity<>(  messageService.findallByChannelId(channelId), HttpStatus.OK);
    }
    @PostMapping( "/reset")
    @Override
    public void reset(){
        messageService.resetMessage();
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<List<MessageReadResponseDto>> readAll(){
        return new ResponseEntity<>(messageService.readAllMessage(), HttpStatus.OK);
    }

}
