package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read-status")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // 생성
    @PostMapping
    public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
        return ResponseEntity.ok(readStatusService.create(request));
    }

    // 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReadStatusDto> find(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(readStatusService.find(id));
    }

    // 특정 user의 모든 읽음 상태 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        readStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
