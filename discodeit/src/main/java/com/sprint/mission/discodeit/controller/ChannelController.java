package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  public ChannelController(ChannelService channelService) {
    this.channelService = channelService;
  }

  // 공개 채널 생성
  @PostMapping
  public ResponseEntity<ChannelDto> createChannel(
      @RequestBody ChannelCreateRequest request) {
    Channel created = channelService.create(request);
    return ResponseEntity.status(201).body(ChannelDto.from(created));
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updateChannel(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request) {
    Channel updated = channelService.update(channelId, request);
    return ResponseEntity.status(201).body(ChannelDto.from(updated));
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> getChannels(@RequestParam UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }

}
