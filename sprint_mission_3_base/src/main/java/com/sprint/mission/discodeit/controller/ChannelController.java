package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.service.ChannelService;
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


    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(@RequestBody CreatePublicChannelRequest request) {
        ChannelDto createdChannel = channelService.createPublic(request);
        return ResponseEntity.status(201).body(createdChannel);
    }


    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivate(@RequestBody CreatePrivateChannelRequest request) {
        ChannelDto createdChannel = channelService.createPrivate(request);
        return ResponseEntity.status(201).body(createdChannel);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ChannelDto> find(@PathVariable UUID id) {
        ChannelDto channel = channelService.find(id);
        return ResponseEntity.ok(channel);
    }


    @GetMapping
    public ResponseEntity<List<ChannelDto>> findByUser(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }


    @PutMapping
    public ResponseEntity<ChannelDto> update(@RequestBody ChannelUpdateRequest request) {
        ChannelDto updatedChannel = channelService.update(request);
        return ResponseEntity.ok(updatedChannel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
