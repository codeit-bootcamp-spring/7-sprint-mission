package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;


    @PostMapping("/public")
    public ChannelDto createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        return channelService.createPublicChannel(request);

    }

    @PostMapping("/private")
    public ChannelDto createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        return channelService.createPrivateChannel(request);
    }

    @GetMapping
    public List<ChannelDto> getAllChannelByUserId(@RequestParam UUID userId) {
        channelService.getAllByUser(userId);
        return channelService.getAllByUser(userId);
    }

    @DeleteMapping("/{channelId}")
    public String removeChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return "삭제 성공";
    }

    @PatchMapping("/{channelId}")
    public ChannelDto updateChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        return channelService.updateChannel(channelId, request);
    }


}
