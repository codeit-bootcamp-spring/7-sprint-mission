package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDto> channelCreate(@RequestBody ChannelRequestDto channelRequestDto) {
        ChannelResponseDto channel = channelService.createChannel(channelRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @RequestMapping(value = "/channelname", method = RequestMethod.PUT)
    public ResponseEntity<ChannelResponseDto> channelNameUpdate(@RequestBody ChannelUpdateDto channelUpdateDto) {
        return ResponseEntity.ok(channelService.updateChannelName(channelUpdateDto));
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> channelDelete(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponseDto>> getAllByUserId(@PathVariable UUID userId) {
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