package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicChannelService;
import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponse;
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

    private final BasicChannelService channelService;


    @PostMapping("/public")
    public ChannelResponse createPublicChannel(@RequestBody ChannelCreateRequest channelCreateRequest){
        return channelService.createChannel(channelCreateRequest, false);

    }

    @PostMapping("/private")
    public ChannelResponse createPrivateChannel(@RequestBody ChannelCreateRequest channelCreateRequest){
        return channelService.createChannel(channelCreateRequest, true);
    }

    @GetMapping
    public List<ChannelResponse> getAllChannelByUserId(@RequestParam UUID userId){
        return channelService.getAllByUser(userId);
    }

    @DeleteMapping("/{channelId}")
    public String removeChannel(@PathVariable UUID channelId){
        channelService.deleteChannel(channelId);
        return "삭제 성공";
    }

    @PatchMapping("/{channelId}")
    public ChannelResponse updateChannel(@PathVariable UUID channelId, @RequestBody ChannelUpdateRequest request){
        return channelService.updateChannel(channelId, request);
    }




}
