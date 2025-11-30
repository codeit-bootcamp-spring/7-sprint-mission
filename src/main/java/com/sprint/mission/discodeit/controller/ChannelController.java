package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ReadStatusService readStatusService;



    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        ChannelDto channelDto = channelService.createPublicChannel(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);

    }

    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        ChannelDto channelDto = channelService.createPrivateChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllChannelByUserId(@RequestParam UUID userId) {
        List<ChannelDto> allByUser = channelService.getAllByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(allByUser);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<?> removeChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("삭제 성공");
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        ChannelDto channelDto = channelService.updateChannel(channelId, request);
        return ResponseEntity.status(HttpStatus.OK).body(channelDto);
    }


}
