package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PrivateChannelRequestDto;
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
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<Channel> channelCreate(
            @Valid @RequestBody PublicChannelRequestDto channelRequestDto) {
        Channel channel = channelService.createPublicChannel(channelRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<Channel> channelCreate(
            @Valid @RequestBody PrivateChannelRequestDto channelRequestDto) {
        Channel channel = channelService.createPrivateChannel(channelRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
    public ResponseEntity<Channel> channelNameUpdate(
            @PathVariable UUID channelId,
            @Valid @RequestBody PublicChannelUpdateDto channelUpdateDto) {
        return ResponseEntity.ok(channelService.updateChannel(channelId, channelUpdateDto));
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> channelDelete(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponseDto>> getAllByUserId(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}

/*
[ ] 공개 채널을 생성할 수 있다.
[ ] 비공개 채널을 생성할 수 있다.
[ ] 공개 채널의 정보를 수정할 수 있다.
[ ] 채널을 삭제할 수 있다.
[ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
 */