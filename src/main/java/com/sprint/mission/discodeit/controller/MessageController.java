package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ChannelService channelService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public MessageReadResponseDto createMessage(@RequestBody MessageCreateRequestDto dto){
        return messageService.createMessage(dto);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public <T>void updateMessage(@RequestBody MessageUpdateRequestDto<T> dto){

        messageService.updateMessage(dto);
    }

    @RequestMapping("/delete")
    public void deleteMessage(@RequestParam UUID messageId){

        messageService.deleteMessage(messageId);
    }

    @RequestMapping(value = "/readChannelMessage", method = RequestMethod.GET)
    public List<MessageReadResponseDto> readChannelMessage(@RequestParam UUID channelId){
        return messageService.findallByChannelId(channelId);
    }

    @RequestMapping("/createChannelMessage")
    public void createChannelMessage(@RequestParam UUID channelId, @RequestBody MessageCreateRequestDto dto){

        messageService.createMessage(dto);
    }

    @RequestMapping(value = "/updateChannelMessage", method = RequestMethod.POST)
    public <T> void  updateChannelMessage(@RequestBody MessageUpdateRequestDto<T> dto){
        messageService.updateMessage(dto);
    }

    @RequestMapping("/readUserMessage")
    public List<MessageReadResponseDto> readUserMessage(@RequestParam UUID userId){

        return messageService.readAllMessageByUserId(userId);
    }

    @RequestMapping("/reset")
    public void reset(){
        messageService.resetMessage();
    }


}
