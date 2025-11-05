package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * [ ] 공개 채널을 생성할 수 있다.                                   /api/channel, POST
 * [ ] 비공개 채널을 생성할 수 있다.                                 /api/channel, POST
 * [ ] 공개 채널의 정보를 수정할 수 있다.                            /api/channel/{channelId}, PUT
 * [ ] 채널을 삭제할 수 있다.                                        /api/channel/{channelId}, DELETE
 * [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.     /api/channel?userId=, GET
 */
@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<?> createChannel(@RequestBody CreatePublicChannelDto createPublicChannelDto) {
        ChannelResponseDto channelResponseDto = channelService.createChannel(createPublicChannelDto);
        return ResponseEntity.ok(channelResponseDto);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<?> createChannel(@RequestBody CreatePrivateChannelDto createPrivateChannelDto) {
        ChannelResponseDto channelResponseDto = channelService.createChannel(createPrivateChannelDto);
        return ResponseEntity.ok(channelResponseDto);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateChannel(@PathVariable UUID channelId, @RequestBody UpdateChannelDto updateChannelDto) {
        ChannelResponseDto channelResponseDto = channelService.updateChannel(channelId, updateChannelDto);
        return ResponseEntity.ok(channelResponseDto);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllChannelByUser(@RequestParam UUID userId) {
        List<ChannelResponseDto> channels = channelService.getAllChannelByUserId(userId);
        return ResponseEntity.ok(channels);
    }

}
