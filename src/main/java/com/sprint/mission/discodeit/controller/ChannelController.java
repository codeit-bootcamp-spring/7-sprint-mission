package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.facade.channel.ChannelCreationFacade;
import com.sprint.mission.discodeit.facade.channel.ChannelDeleteFacade;
import com.sprint.mission.discodeit.facade.channel.ChannelDetailViewFacade;
import com.sprint.mission.discodeit.facade.channel.ChannelOverViewFacade;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelCreationFacade channelCreationFacade;
    private final ChannelOverViewFacade channelOverViewFacade;
    private final ChannelDetailViewFacade channelDetailViewFacade;
    private final ChannelDeleteFacade channelDeleteFacade;
    private final ChannelService channelService;

    //채널 목록 조회 : 공개방은 전부, 비밀방은 내 기준 참여되어있는 것 기준
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelInfoRes>> getAllChannels(@RequestHeader("X-LOGINUSER-ID") UUID userId){
        return ResponseEntity.ok(channelOverViewFacade.findAllMyChannels(userId));
    }

    // 공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<Void> createPublicChannel(@RequestHeader("X-LOGINUSER-ID") UUID managerId, @RequestBody ChannelCreateReq req){
        Channel channel = channelCreationFacade.createPublicChannel(managerId, req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(channel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    //비밀 채널 생성
    @RequestMapping(method = RequestMethod.POST, value="/private")
    public ResponseEntity<Void> createPrivateChannel(@RequestHeader("X-LOGINUSER-ID") UUID managerId, @RequestBody ChannelCreateSecReq req){
        Channel channel = channelCreationFacade.createPrivateChannel(managerId, req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(channel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    //공개 채널 수정
    @RequestMapping(method = RequestMethod.PUT, value = "/{channelId}")
    public ResponseEntity<Void> updatePublicChannel(@PathVariable UUID channelId, @RequestBody ChannelUpdateReq req){
        channelService.update(channelId, req);
        return ResponseEntity.noContent().build();
    }
    
    //채널 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId){
        channelDeleteFacade.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }
}
