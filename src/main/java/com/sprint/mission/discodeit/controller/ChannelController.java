package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
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
    @RequestMapping(value = "/channels/public", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPublic(@RequestBody CreatePublicChannelRequestDto requestDto) {
        Channel createdChannel = channelService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(value = "/channels/private", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPrivate(@RequestBody CreatePrivateChannelRequestDto requestDto) {
        Channel createdChannel = channelService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(value = "/channels/{channelId}")
    public ResponseEntity<Channel> update(@PathVariable UUID channelId,
                                          @RequestBody UpdatePublicChannelRequestDto request) {
        Channel udpatedChannel = channelService.update(channelId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(udpatedChannel);
    }

    // 채널 삭제
    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @RequestMapping(value = "/channels", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> searchChannels(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }

    //--------------------- 기존의 메서드(심화 요구사항에서 사용하지 않아 변경X) ---------------------//

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
}
