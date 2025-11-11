package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelControllerDocs{

    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    @Override
    public ResponseEntity<ChannelReadResponseDto> createPrivateChannel(@RequestBody ChannelPrivateCreateRequestDto dto){
       ;
        return new ResponseEntity<>(channelService.createPrivateChannel(dto), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    @Override
    public ResponseEntity<ChannelReadResponseDto> createPublicChannel(@RequestBody ChannelPublicCreateRequestDto dto){
       return new ResponseEntity<>(channelService.createPublicChannel(dto), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    @Override
    public void deleteChannel(@PathVariable UUID channelId){
        channelService.deleteChannel(channelId);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<ChannelReadResponseDto>> readChannelById(@RequestParam UUID userId){
        return new ResponseEntity<>(channelService.findAllByUserId(userId),HttpStatus.OK);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
    @Override
    public ResponseEntity<ChannelReadResponseDto> patchChannel(@PathVariable UUID channelId, @RequestBody ChannelPatchRequestDto dto){
        return new ResponseEntity<>(channelService.patchChannel(dto,channelId),HttpStatus.OK);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @Override
    public void reset(){
        channelService.resetChannelRepository();
    }

    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    @Override
    public ResponseEntity<List<ChannelReadResponseDto>> readAll(){
        return new ResponseEntity<>(channelService.readAllChannel(),HttpStatus.OK);
    }

/// //////////////////////////////////////////////////////////////////////////////////////////////////
///
//    private <ChannelResponseDto> ApiResponseDto<ChannelReadResponseDto> toApiResponseDto(ChannelReadResponseDto dto){
//        return ApiResponseDto.success(dto);
//    }
//
//    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.PATCH)
//    public <T> void updateChannel(@RequestBody ChannelUpdateRequestDto<T>  dto, @PathVariable UUID channelId){
//        channelService.updateChannel(dto);
//    }
//

//
//    @RequestMapping("/readAll")
//    public ResponseEntity<List<ApiResponseDto<ChannelReadResponseDto>>> readAll(){
//        List<ApiResponseDto<ChannelReadResponseDto>> responseDtoList = channelService.readAllChannel().stream().map(this::toApiResponseDto).toList();
//        return new ResponseEntity<List<ApiResponseDto<ChannelReadResponseDto>>>(responseDtoList,HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/userEnterChannel", method = RequestMethod.GET)
//    public void userEnterChannel(@RequestParam UUID userId, @RequestParam UUID channelId){
//        channelService.inviteUserToChannel(userId,channelId);
//    }
//
//    @RequestMapping(value = "/userExitChannel", method = RequestMethod.GET)
//    public void userExitChannel(@RequestParam UUID userId, @RequestParam UUID channelId){
//        channelService.deleteUserFromChannel(userId,channelId);
//    }
}
