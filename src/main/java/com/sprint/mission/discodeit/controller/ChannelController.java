package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

    @PostMapping( "/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(@Valid @RequestBody ChannelPrivateCreateRequestDto dto){
       ;
        return new ResponseEntity<>(channelService.createPrivateChannel(dto), HttpStatus.CREATED);
    }

    @PostMapping( "/public")
    public ResponseEntity<ChannelDto> createPublicChannel(@Valid @RequestBody ChannelPublicCreateRequestDto dto){
       return new ResponseEntity<>(channelService.createPublicChannel(dto), HttpStatus.CREATED);
    }


    @DeleteMapping( "/{channelId}")
    public void deleteChannel(@PathVariable UUID channelId){
        channelService.deleteChannel(channelId);
    }

    @GetMapping( "")
    public ResponseEntity<List<ChannelDto>> readChannelById(@RequestParam UUID userId){
        return new ResponseEntity<>(channelService.findAllByUserId(userId),HttpStatus.OK);
    }

    @PatchMapping( "/{channelId}")
    public ResponseEntity<ChannelDto> patchChannel(@PathVariable UUID channelId, @Valid @RequestBody ChannelPatchRequestDto dto){
        return new ResponseEntity<>(channelService.patchChannel(dto,channelId),HttpStatus.OK);
    }

    @PostMapping("/reset")
    public void reset(){
        channelService.resetChannelRepository();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ChannelDto>> readAll(){
        return new ResponseEntity<>(channelService.readAllChannel(),HttpStatus.OK);
    }
}
