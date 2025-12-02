package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll() {
        return ResponseEntity.ok(channelService.findAll());
    }
    // 채널 생성
    @PostMapping
    public ResponseEntity<ChannelDto> create(@RequestBody ChannelCreateRequest request) {
        ChannelDto created = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 채널 수정
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> update(
            @PathVariable("channelId") UUID channelId,
            @RequestBody ChannelUpdateRequest request
    ) {
        // ChannelUpdateRequest는 id + name 사용
        ChannelUpdateRequest fixedRequest = new ChannelUpdateRequest(channelId, request.name());

        ChannelDto updated = channelService.update(fixedRequest);
        return ResponseEntity.ok(updated);
    }

    // 채널 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    // 특정 채널 조회
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDto> find(@PathVariable("channelId") UUID channelId) {
        ChannelDto dto = channelService.find(channelId);
        return ResponseEntity.ok(dto);
    }
}
