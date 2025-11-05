package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/createPrivate",method = RequestMethod.POST)
    public ChannelReadResponseDto createPrivateChannel(@RequestBody ChannelPrivateCreateRequestDto dto){
      return channelService.createPrivateChannel(dto);
    }

    @RequestMapping(value = "/createPublic", method = RequestMethod.POST)
    public ChannelReadResponseDto createPublicChannel(@RequestBody ChannelPublicCreateRequestDto dto){
       return channelService.createPublicChannel(dto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public <T> void updateChannel(@RequestBody ChannelUpdateRequestDto<T>  dto){
        channelService.updateChannel(dto);
    }

    @RequestMapping("/delete")
    public void deleteChannel(@RequestParam UUID channelId){
        channelService.deleteChannel(channelId);
    }

    @RequestMapping(value = "/readChannelById", method = RequestMethod.GET)
    public List<ChannelReadResponseDto> readChannelById(@RequestParam UUID userId){
      return channelService.findAllByUserId(userId);
    }
    @RequestMapping("/reset")
    public void reset(){
        channelService.resetChannelRepository();
    }

    @RequestMapping("/readAll")
    public List<ChannelReadResponseDto> readAll(){
        return channelService.readAllChannel();
    }

    @RequestMapping(value = "/userEnterChannel", method = RequestMethod.GET)
    public void userEnterChannel(@RequestParam UUID userId, @RequestParam UUID channelId){
        channelService.inviteUserToChannel(userId,channelId);
    }

    @RequestMapping(value = "/userExitChannel", method = RequestMethod.GET)
    public void userExitChannel(@RequestParam UUID userId, @RequestParam UUID channelId){
        channelService.deleteUserFromChannel(userId,channelId);
    }
}
