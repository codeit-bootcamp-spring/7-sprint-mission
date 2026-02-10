package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@EnableMethodSecurity
public class ChannelController {
    private final ChannelService channelService;

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(@Valid @RequestBody PublicChannelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPublicChannel(request));
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @PostMapping( "/private")
    public ResponseEntity<ChannelDto> createPrivate(@Valid @RequestBody PrivateChannelCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPrivateChannel(request));
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updatePublic(@PathVariable UUID channelId, @Valid @RequestBody ChannelUpdateRequest request) {
        return ResponseEntity.ok().body(channelService.update(channelId, request));
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> removeChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> getVisibleChannel(@RequestParam UUID userId) {
        return ResponseEntity.ok(channelService.getAllVisibleByUser(userId));
    }
}