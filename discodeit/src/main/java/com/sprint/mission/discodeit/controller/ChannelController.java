package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
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
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    // 공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<ChannelDto> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        Channel created = channelService.create(request);
        return ResponseEntity.ok(ChannelDto.from(created));
    }

    // 비공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel created = channelService.create(request);
        return ResponseEntity.ok(ChannelDto.from(created));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable("channelId") java.util.UUID channelId,
                                                    @RequestBody PublicChannelUpdateRequest request) {
        Channel updated = channelService.update(channelId, request);
        return ResponseEntity.ok(ChannelDto.from(updated));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<ChannelDto> deleteChannel(@PathVariable("channelId") java.util.UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> getAllChannels(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }

}
