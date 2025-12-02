package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusControllerDocs {
    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보 생성
    @PostMapping
    public ResponseEntity<ReadStatusResponseDto> createReadStatus(@RequestBody CreateReadStatusRequestDto requestDto) {
        ReadStatusResponseDto createdReadStatus = readStatusService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
    }

    // 특정 채널의 메시지 수신 정보 수정
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResponseDto> createReadStatus(@PathVariable UUID readStatusId, @RequestBody UpdateReadStatusRequestDto requestDto) {
        ReadStatusResponseDto updatedReadStatus = readStatusService.update(readStatusId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }

    // 특정 사용자의 메시지 수신 정보 조회
    @GetMapping
    public ResponseEntity<List<ReadStatusResponseDto>> search(@RequestParam UUID userId) {
        List<ReadStatusResponseDto> readStatusList = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }
}
