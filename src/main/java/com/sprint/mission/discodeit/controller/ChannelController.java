package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    //공개 채널을 만들 수 있다.
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDto> createPublicChannel(@RequestBody CreatePublicChannelDto request) {
        ChannelResponseDto publicChannel = channelService.createPublicChannel(request);
        return ResponseEntity.ok(publicChannel);
    }

    //비공개 채널을 만들 수 있다.
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDto> createPrivateChannel(@RequestBody CreatePrivateChannelDto request) {
        ChannelResponseDto privateChannel = channelService.createPrivateChannel(request);
        return ResponseEntity.ok(privateChannel);
    }

    //공개 채널의 정보를 수정할 수 있다.
    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<ChannelResponseDto> updatePublicChannel(@PathVariable UUID channelId, @RequestBody UpdateChannelDto request) {
        ChannelResponseDto response = channelService.updateChannel(channelId, request);
        return ResponseEntity.ok(response);
    }

    //채널 조회
    @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<ChannelResponseDto> findChannel(@PathVariable UUID channelId) {
        ChannelResponseDto response = channelService.find(channelId);
        return ResponseEntity.ok(response);
    }

    //채널을 삭제할 수 있다.
    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
    @RequestMapping(value = "by-user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponseDto>> findUserAllChannels(@PathVariable UUID userId) {
        List<ChannelResponseDto> response = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(response);
    }

}
