package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createPublic(@Valid @RequestBody PublicChannelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPublicChannel(request));
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createPrivate(@Valid @RequestBody PrivateChannelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPrivateChannel(request));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ChannelResponse> updatePublic(@Valid @RequestBody ChannelUpdateRequest request) {
        return ResponseEntity.ok().body(channelService.update(request));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@Valid @RequestBody ChannelDeleteRequest request) {
        channelService.delete(request);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponse>> getVisibleChannel(@PathVariable String userId) {
        return ResponseEntity.ok(channelService.getAllVisibleByUserId(userId));
    }

}
