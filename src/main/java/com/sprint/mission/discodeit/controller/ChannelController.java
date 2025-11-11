package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.global.util.ApiResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * [ ] 공개 채널을 생성할 수 있다.                                   /api/channels, POST
 * [ ] 비공개 채널을 생성할 수 있다.                                 /api/channels, POST
 * [ ] 공개 채널의 정보를 수정할 수 있다.                            /api/channels/{channelId}, PUT
 * [ ] 채널을 삭제할 수 있다.                                        /api/channels/{channelId}, DELETE
 * [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.     /api/channels?userId=, GET
 */
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<ChannelResponseDto>> createChannel(@Valid @RequestBody CreatePublicChannelDto createPublicChannelDto) {
        ChannelResponseDto channelResponseDto = channelService.createChannel(createPublicChannelDto);
        ApiResponse<ChannelResponseDto> responseBody = ApiResponse.success(channelResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<ChannelResponseDto>> createChannel(@Valid @RequestBody CreatePrivateChannelDto createPrivateChannelDto) {
        ChannelResponseDto channelResponseDto = channelService.createChannel(createPrivateChannelDto);
        ApiResponse<ChannelResponseDto> responseBody = ApiResponse.success(channelResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<ChannelResponseDto>> updateChannel(@PathVariable UUID channelId, @RequestBody UpdateChannelDto updateChannelDto) {
        ChannelResponseDto channelResponseDto = channelService.updateChannel(channelId, updateChannelDto);
        ApiResponse<ChannelResponseDto> responseBody = ApiResponse.success(channelResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ChannelResponseDto>>> getAllChannelByUser(@RequestParam UUID userId) {
        List<ChannelResponseDto> channels = channelService.getAllChannelByUserId(userId);
        ApiResponse<List<ChannelResponseDto>> responseBody = ApiResponse.success(channels);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
