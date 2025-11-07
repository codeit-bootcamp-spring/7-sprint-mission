package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.response.CustomApiResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
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
    @RequestMapping(method = RequestMethod.GET, value ="/list")
    public ResponseEntity<CustomApiResponse<List<ChannelInfoRes>>> getAllChannels(@RequestHeader("X-LOGINUSER-ID") UUID userId){
        return ResponseEntity.ok(CustomApiResponse.success(channelOverViewFacade.findAllMyChannels(userId)));
    }

    // 공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<CustomApiResponse<Void>> createPublicChannel(@RequestHeader("X-LOGINUSER-ID") UUID managerId, @RequestBody ChannelCreateReq req){
        Channel channel = channelCreationFacade.createPublicChannel(managerId, req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(channel.getId())
                .toUri();

        log.info("공개 채널 생성: id = {}, name = {}, discription = {}, 생성한 사람 id = {}",
                channel.getId(), channel.getName(), channel.getDescription(), channel.getManagerId());

        return ResponseEntity.created(location).body(CustomApiResponse.success());
    }
    
    //비밀 채널 생성
    @RequestMapping(method = RequestMethod.POST, value="/private")
    public ResponseEntity<CustomApiResponse<Void>> createPrivateChannel(@RequestHeader("X-LOGINUSER-ID") UUID managerId, @RequestBody ChannelCreateSecReq req){
        Channel channel = channelCreationFacade.createPrivateChannel(managerId, req);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(channel.getId())
                .toUri();

        log.info("비밀 채널 생성: id = {}, 참여인들 id = {}, 생성한 사람 id = {}",
                channel.getId(), channel.getUserIds(), channel.getManagerId());

        return ResponseEntity.created(location).body(CustomApiResponse.success());
    }

    //공개 채널 수정
    @RequestMapping(method = RequestMethod.PUT, value = "/{channelId}")
    public ResponseEntity<CustomApiResponse<Void>> updatePublicChannel(@PathVariable UUID channelId, @RequestBody ChannelUpdateReq req){
        channelService.update(channelId, req);

        log.info("공개 채널 수정: id = {}, name = {}, description = {}",
                channelId, req.name(), req.description());

        return ResponseEntity.ok(CustomApiResponse.success());
    }
    
    //채널 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/{channelId}")
    public ResponseEntity<CustomApiResponse<Void>> deleteChannel(@PathVariable UUID channelId){
        log.info("채널 삭제 id : {}", channelId);
        channelDeleteFacade.deleteChannel(channelId);
        return ResponseEntity.ok(CustomApiResponse.success());
    }
}
