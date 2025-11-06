package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
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
    public ResponseEntity<String> createChannel(@RequestParam UUID userId,
                                                @RequestBody CreateChannelRequestDto requestDto) {
        channelService.create(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("channel created");
    }

    // 채널에 멤버 추가(비공개 채널만 가능)
    @RequestMapping(value = "/channels/{channelId}/members", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateChannelMember(@PathVariable UUID channelId,
                                                      @RequestBody UpdateChannelRequestDto requestDto) {
        channelService.addMember(channelId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("channel member updated");
    }

    // 채널 관리자 변경
    @RequestMapping(value = "/channels/{channelId}/admin", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateChannelAdmin(@PathVariable UUID channelId,
                                                     @RequestBody UpdateChannelAdminRequestDto requestDto) {
        channelService.updateAdmin(channelId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("channel admin updated");
    }

    // 채널 이름 변경
    @RequestMapping(value = "/channels/{channelId}/name", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateChannelName(@PathVariable UUID channelId,
                                                    @RequestBody UpdateChannelNameRequestDto requestDto) {
        channelService.updateName(channelId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("channel name updated");
    }

    // 채널 삭제
    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteChannel(@PathVariable UUID channelId,
                                                @RequestBody DeleteChannelRequestDto requestDto) {
        channelService.delete(channelId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("channel deleted");
    }

    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @RequestMapping(value = "/channels", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponseDto>> searchChannels(@RequestParam UUID userId) {
        List<ChannelResponseDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
