package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelController implements ChannelControllerDocs {

  private final ChannelService channelService;

  //공개 채널을 만들 수 있다.
  @PostMapping(value = "/public")
  public ResponseEntity<ChannelResponseDto> createPublicChannel(
      @Valid @RequestBody CreatePublicChannelDto request) {
    log.info("POST /api/channels/public - 공개 채널 생성 요청");
    ChannelResponseDto publicChannel = channelService.createPublicChannel(request);
    return ResponseEntity.ok(publicChannel);
  }

  //비공개 채널을 만들 수 있다.
  @PostMapping(value = "/private")
  public ResponseEntity<ChannelResponseDto> createPrivateChannel(
      @Valid @RequestBody CreatePrivateChannelDto request) {
    log.info("POST /api/channels/private - 비공개 채널 생성 요청");
    ChannelResponseDto privateChannel = channelService.createPrivateChannel(request);
    return ResponseEntity.ok(privateChannel);
  }

  //공개 채널의 정보를 수정할 수 있다.
  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<ChannelResponseDto> updatePublicChannel(
      @PathVariable UUID channelId,
      @RequestBody UpdateChannelDto request) {
    log.info("PATCH /api/channels/{} - 공개 채널 수정 요청", channelId);
    ChannelResponseDto response = channelService.updateChannel(channelId, request);
    return ResponseEntity.ok(response);
  }

  //채널 조회
  // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
  // api/channels?userId=...
  @GetMapping
  public ResponseEntity<List<ChannelResponseDto>> findUserAllChannels(
      @RequestParam UUID userId) {
    List<ChannelResponseDto> response = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }

  //채널을 삭제할 수 있다.
  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable UUID channelId) {
    log.info("DELETE /api/channels/{} - 채널 삭제 요청", channelId);
    channelService.deleteChannel(channelId);
    return ResponseEntity.noContent().build();
  }

}
