package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    private ResponseEntity<ChannelResponse> createPublic(PublicChannelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPublicChannel(request));
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    private ResponseEntity<ChannelResponse> createPrivate(PrivateChannelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPrivateChannel(request));
    }

    @RequestMapping(method = RequestMethod.PUT)
    private ResponseEntity<ChannelResponse> updatePublic(ChannelUpdateRequest request) {
        return ResponseEntity.ok().body(channelService.update(request));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    private ResponseEntity<Void> deleteChannel(ChannelDeleteRequest request) {
        channelService.delete(request);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    private ResponseEntity<List<ChannelResponse>> getVisibleChannel(@PathVariable String userId) {
        return ResponseEntity.ok(channelService.getAllVisibleByUserId(userId));
    }

}
