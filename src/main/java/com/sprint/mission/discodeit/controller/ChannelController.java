package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelControllerDocs {
    private final ChannelService channelService;

    // 채널 생성
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublic(@Valid @RequestBody CreatePublicChannelRequestDto requestDto) {
        ChannelDto createdChannel = channelService.create(requestDto);

        log.info("POST /api/channels/public - 공개 채널 생성 완료: channelId = {}, channelName = {}",
                createdChannel.id(), createdChannel.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivate(@RequestBody CreatePrivateChannelRequestDto requestDto) {
        ChannelDto createdChannel = channelService.create(requestDto);

        log.info("POST /api/channels/private - 비공개 채널 생성 완료: channelId = {}", createdChannel.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    // 채널 수정
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
                                             @RequestBody UpdatePublicChannelRequestDto request) {
        ChannelDto updatedChannel = channelService.update(channelId, request);

        log.info("PATCH /api/channels/{} - 공개 채널 정보 수정 완료", channelId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }

    // 채널 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);

        log.info("DELETE /api/channels/{} - 채널 삭제 완료", channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @GetMapping
    public ResponseEntity<List<ChannelDto>> searchChannels(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
