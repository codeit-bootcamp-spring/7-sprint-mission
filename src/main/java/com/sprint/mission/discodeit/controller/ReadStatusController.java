package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // 수신 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(
            @Valid @RequestBody ReadStatusCreateRequest readStatusRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.createReadStatus(readStatusRequestDto));
    }

    // 수신 정보 수정
    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatus> readStatusUpdate(
            @PathVariable UUID readStatusId,
            @Valid @RequestBody ReadStatusUpdateRequest readStatusUpdateDto) {

        return ResponseEntity.ok(readStatusService.updateReadStatus(readStatusId ,readStatusUpdateDto));
    }

    // 사용자의 메시지 수신 정보 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusDto>> getAllByUserId(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}

/*
메시지 수신 정보 관리
[ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다. -> 채널에서 작동함
[ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
[ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
 */