package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.readStatusDto.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // 수신 정보 수정
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ReadStatusResponseDto> readStatusUpdate(@RequestBody ReadStatusUpdateDto readStatusUpdateDto) {
        return ResponseEntity.ok(readStatusService.updateReadStatus(readStatusUpdateDto));
    }

    // 사용자의 메시지 수신 정보 조회
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDto>> getAllByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}

/*
메시지 수신 정보 관리
[ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다. -> 채널에서 작동함
[ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
[ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
 */