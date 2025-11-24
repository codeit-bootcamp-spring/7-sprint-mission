package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.service.BasicChannelService;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelControllerDocs {

    private final BasicChannelService channelService;


    @PostMapping("/public")
    public ChannelResponse createPublicChannel(@RequestBody PublicChannelCreateRequest request){
        return channelService.createPublicChannel(request);

    }

    @PostMapping("/private")
    public ChannelResponse createPrivateChannel(@RequestBody PrivateChannelCreateRequest request){
        return channelService.createPrivateChannel(request);
    }

    @GetMapping
    public List<ChannelResponse> getAllChannelByUserId(@RequestParam UUID userId){
        channelService.getAllByUser(userId);
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
