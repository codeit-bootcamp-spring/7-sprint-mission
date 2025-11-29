package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.ReadStateControllerDocs;
import com.sprint.mission.discodeit.dto.channelmember.request.ChannelMemberCreateReq;
import com.sprint.mission.discodeit.dto.channelmember.response.ChannelMemberInfoRes;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.facade.channelmember.ChannerMemberCreateFacade;
import com.sprint.mission.discodeit.service.ChannerlMemberService;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel-members")
@RequiredArgsConstructor
public class ChannelMemberController implements ReadStateControllerDocs {

  private final ChannerMemberCreateFacade channerMemberCreateFacade;
  private final ChannerlMemberService channerlMemberService;

  //메세지 수신 정보 생성
  @PostMapping
  public ResponseEntity<ChannelMemberInfoRes> createReadStatus(
      @RequestBody ChannelMemberCreateReq req) {
    ChannelMember channelMember = channerMemberCreateFacade.create(req);

    return ResponseEntity.created(URI.create("/api/channel-members" + channelMember.getId()))
        .body(ChannelMemberInfoRes.from(channelMember));
  }

  //메세지 수신 정보 업데이트
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ChannelMemberInfoRes> updateReadStatus(@PathVariable UUID readStatusId) {
    ChannelMember channelMember = channerlMemberService.update(readStatusId);
    return ResponseEntity.ok(ChannelMemberInfoRes.from(channelMember));
  }

  //메세지 수신 정보 조회
  @GetMapping("/{readStatusId}")
  public ResponseEntity<ChannelMemberInfoRes> getReadStatus(@PathVariable UUID readStatusId) {
    return ResponseEntity.ok(channerlMemberService.findById(readStatusId));
  }
}
