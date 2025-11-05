package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

   //특정 채널의 메시지 수신 정보를 생성할 수 있다.
    @RequestMapping(value = "/channels/{channelId}/read-status", method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponseDto> ChannelByCreateReadStatus(
            @PathVariable UUID channelId,
            @RequestBody CreateReadStatusRequestDto request) {
        ReadStatusResponseDto response = readStatusService.createReadStatus(channelId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


   //특정 채널의 메시지 수신 정보를 수정할 수 있다.
    //channels/.../read-status
    @RequestMapping(value = "/channels/{channelId}/read-status", method = RequestMethod.PUT)
    public ResponseEntity<ReadStatusResponseDto> ChannelByUpdateReadStatus(
            @PathVariable UUID channelId,
            @RequestBody UpdateReadStatusDto request) {
        ReadStatusResponseDto response = readStatusService.updateReadStatus(channelId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

   //특정 사용자의 메시지 수신 정보를 조회할 수 있다.
    @RequestMapping(value = "/users/{userId}/read-status", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDto>> UserByFindMessage(@PathVariable UUID userId) {
        List<ReadStatusResponseDto> response = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
