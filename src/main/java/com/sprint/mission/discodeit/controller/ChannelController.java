package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.ChannelDocs;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
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
public class ChannelController implements ChannelDocs {

  private final ChannelService channelService;

  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ResponseEntity<ChannelResponseDto> createChannel(
      @Valid @RequestBody CreatePublicChannelDto createPublicChannelDto) {
    ChannelResponseDto createChannelResponseDto = channelService.createChannel(
        createPublicChannelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createChannelResponseDto);
  }

  @RequestMapping(value = "/private", method = RequestMethod.POST)
  public ResponseEntity<ChannelResponseDto> createChannel(
      @Valid @RequestBody CreatePrivateChannelDto createPrivateChannelDto) {
    ChannelResponseDto createChannelResponseDto = channelService.createChannel(
        createPrivateChannelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createChannelResponseDto);
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
  public ResponseEntity<ChannelResponseDto> updateChannel(@PathVariable UUID channelId,
      @RequestBody UpdateChannelDto updateChannelDto) {
    ChannelResponseDto channelResponseDto = channelService.updateChannel(channelId,
        updateChannelDto);
    return ResponseEntity.status(HttpStatus.OK).body(channelResponseDto);
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ChannelResponseDto>> getAllChannelByUser(@RequestParam UUID userId) {
    List<ChannelResponseDto> channelsDto = channelService.getAllChannelByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(channelsDto);
  }

}
