package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.global.dto.ApiResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChannelController {
    private final ChannelService channelService;

    // 채널 생성
    @RequestMapping(value = "/channels", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Object>> createChannel(@RequestParam UUID userId,
                                                              @RequestBody CreateChannelRequestDto requestDto) {
        channelService.create(userId, requestDto);
        return ApiResponse.success(HttpStatus.CREATED,"채널이 생성되었습니다.");
    }

    // 채널에 멤버 추가(비공개 채널만 가능)
    @RequestMapping(value = "/channels/{channelId}/members", method = RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<Object>> updateChannelMember(@PathVariable UUID channelId,
                                                      @RequestBody UpdateChannelRequestDto requestDto) {
        channelService.addMember(channelId, requestDto);
        return ApiResponse.success(HttpStatus.OK, "채널에 사용자가 추가되었습니다.");
    }

    // 채널 관리자 변경
    @RequestMapping(value = "/channels/{channelId}/admin", method = RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<Object>> updateChannelAdmin(@PathVariable UUID channelId,
                                                     @RequestBody UpdateChannelAdminRequestDto requestDto) {
        channelService.updateAdmin(channelId, requestDto);
        return ApiResponse.success(HttpStatus.OK, "채널 관리자가 변경되었습니다.");
    }

    // 채널 이름 변경
    @RequestMapping(value = "/channels/{channelId}/name", method = RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<Object>> updateChannelName(@PathVariable UUID channelId,
                                                    @RequestBody UpdateChannelNameRequestDto requestDto) {
        channelService.updateName(channelId, requestDto);
        return ApiResponse.success(HttpStatus.OK, "채널 이름이 변경되었습니다.");
    }

    // 채널 삭제
    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<Object>> deleteChannel(@PathVariable UUID channelId,
                                                @RequestBody DeleteChannelRequestDto requestDto) {
        channelService.delete(channelId, requestDto);
        return ApiResponse.success(HttpStatus.OK, "채널이 삭제되었습니다.");
    }

    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @RequestMapping(value = "/channels", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ChannelResponseDto>>> searchChannels(@RequestParam UUID userId) {
        List<ChannelResponseDto> channels = channelService.findAllByUserId(userId);
        return ApiResponse.success(HttpStatus.OK, "사용자 채널 목록 조회", channels);
    }
}
