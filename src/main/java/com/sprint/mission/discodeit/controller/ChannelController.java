package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.facade.channel.ChannelCreationFacade;
import com.sprint.mission.discodeit.facade.channel.ChannelDeleteFacade;
import com.sprint.mission.discodeit.facade.channel.ChannelOverViewFacade;
import com.sprint.mission.discodeit.facade.channel.ChannelUpdateFacade;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelCreationFacade channelCreationFacade;
  private final ChannelOverViewFacade channelOverViewFacade;
  private final ChannelDeleteFacade channelDeleteFacade;
  private final ChannelUpdateFacade channelUpdateFacade;

  //채널 목록 조회 : 공개방은 전부, 비밀방은 내 기준 참여되어있는 것 기준
  @GetMapping
  public ResponseEntity<Map<ChannelType, List<ChannelInfoRes>>> getAllChannels(
      @RequestHeader("X-LOGINUSER-ID") UUID userId,
      @RequestParam(required = false) String searchTxt) {
    return ResponseEntity.ok(channelOverViewFacade.findAllMyChannels(userId, searchTxt));
  }

  // 공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<ChannelPublicInfoRes> createPublicChannel(
      @RequestHeader("X-LOGINUSER-ID") UUID managerId,
      @Valid @RequestBody ChannelCreateReq req) {
    ChannelPublicInfoRes channel = (ChannelPublicInfoRes) channelCreationFacade.createPublicChannel(
        managerId, req);
    return ResponseEntity.created(URI.create("/api/channels/" + channel.channelId())).body(channel);
  }

  //비밀 채널 생성
  @PostMapping("/private")
  public ResponseEntity<ChannelPrivateInfoRes> createPrivateChannel(
      @RequestHeader("X-LOGINUSER-ID") UUID managerId,
      @Valid @RequestBody ChannelCreateSecReq req) {
    ChannelPrivateInfoRes channel = (ChannelPrivateInfoRes) channelCreationFacade.createPrivateChannel(
        managerId, req);
    return ResponseEntity.created(URI.create("/api/channels/" + channel.channelId())).body(channel);
  }

  //공개 채널 수정
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelInfoRes> updatePublicChannel(@PathVariable UUID channelId,
      @Valid @RequestBody ChannelUpdateReq req) {
    return ResponseEntity.ok(channelUpdateFacade.update(channelId, req));
  }

  //채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelDeleteFacade.deleteChannel(channelId);
    return ResponseEntity.noContent().build();
  }
}
