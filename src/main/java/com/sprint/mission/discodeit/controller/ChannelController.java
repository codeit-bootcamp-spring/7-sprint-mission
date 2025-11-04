package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    // 채널 생성
    @RequestMapping("/create/{userId}")
    public ResponseEntity<String> createChannel(@PathVariable UUID userId,
                                                @RequestBody CreateChannelRequestDto requestDto) {
        try {
            channelService.create(userId, requestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("channel created");
    }

    // 채널에 멤버 추가(비공개 채널만 가능)
    @RequestMapping("/update/member/{channelId}")
    public ResponseEntity<String> updateChannelMember(@PathVariable UUID channelId,
                                                      @RequestBody UpdateChannelRequestDto requestDto) {
        try {
            channelService.addMember(channelId, requestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("channel member updated");
    }

    // 채널 관리자 변경
    @RequestMapping("/update/admin/{channelId}")
    public ResponseEntity<String> updateChannelAdmin(@PathVariable UUID channelId,
                                                     @RequestBody UpdateChannelAdminRequestDto requestDto) {
        try {
            channelService.updateAdmin(channelId, requestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("channel admin updated");
    }

    // 채널 이름 변경
    @RequestMapping("/update/name/{channelId}")
    public ResponseEntity<String> updateChannelName(@PathVariable UUID channelId,
                                                    @RequestBody UpdateChannelNameRequestDto requestDto) {
        try {
            channelService.updateName(channelId, requestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("channel name updated");
    }

    // 채널 삭제
    @RequestMapping("/delete/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable UUID channelId,
                                                @RequestBody DeleteChannelRequestDto requestDto) {
        try {
            channelService.delete(channelId, requestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("channel deleted");
    }

    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @RequestMapping("/search/{userId}")
    public ResponseEntity<List<ChannelResponseDto>> searchChannels(@PathVariable UUID userId) {
        List<ChannelResponseDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
