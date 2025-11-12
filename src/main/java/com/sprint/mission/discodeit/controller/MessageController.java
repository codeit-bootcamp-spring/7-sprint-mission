package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
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
public class MessageController {

    private final MessageService messageService;
    private final ChannelService channelService;

    @RequestMapping(value = "", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageReadResponseDto> createMessage(@RequestPart("messageCreateRequest") MessageCreateRequestDto dto,
                                                                @RequestPart(value = "attachments",required = false) List<MultipartFile> attachments
                                                                                ){

        return new ResponseEntity<>(messageService.createMessage(dto, attachments), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<MessageReadResponseDto> patchMessage(@PathVariable UUID messageId, @RequestBody MessagePatchRequestDto dto){
        MessageReadResponseDto mRRDto = messageService.patchMessage(dto, messageId);
        return new ResponseEntity<>(mRRDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{messageId}",method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable UUID messageId){
        messageService.deleteMessage(messageId);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<MessageReadResponseDto>> readChannelMessage(@RequestParam UUID channelId){
        return new ResponseEntity<>(  messageService.findallByChannelId(channelId), HttpStatus.OK);
    }

    @RequestMapping(value = "/reset",method = RequestMethod.GET)
    public void reset(){
        messageService.resetMessage();
    }

    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity<List<MessageReadResponseDto>> readAll(){
        return new ResponseEntity<>(messageService.readAllMessage(), HttpStatus.OK);
    }

///  ////////////////////////////////////////////////////////////////////

//    @RequestMapping(value = "update", method = RequestMethod.POST)
//    public <T>void updateMessage(@RequestBody MessageUpdateRequestDto<T> dto){
//
//        messageService.updateMessage(dto);
//    }
//
//    @RequestMapping("/readUserMessage")
//    public ResponseEntity<List<ApiResponseDto<MessageReadResponseDto>>> readUserMessage(@RequestParam UUID userId){
//        List<ApiResponseDto<MessageReadResponseDto>> apiResponseDtoList = messageService.readAllMessageByUserId(userId).stream().map(ApiResponseDto::success).toList();
//        return new ResponseEntity<List<ApiResponseDto<MessageReadResponseDto>>>(apiResponseDtoList, HttpStatus.OK );
//    }

//


}
