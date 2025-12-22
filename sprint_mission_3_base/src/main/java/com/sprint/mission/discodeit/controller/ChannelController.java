package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @Override
    @PostMapping
    public ResponseEntity<ChannelDto> create(@Valid @RequestBody ChannelCreateRequest request) {
        ChannelDto createdChannel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @Override
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> update(
            @PathVariable UUID channelId,
            @Valid @RequestBody ChannelUpdateRequest request
    ) {
        ChannelDto updatedChannel = channelService.update(channelId, request);
        return ResponseEntity.ok(updatedChannel);
    }

    @Override
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll() {
        List<ChannelDto> channels = channelService.findAll();
        return ResponseEntity.ok(channels);
    }
}
