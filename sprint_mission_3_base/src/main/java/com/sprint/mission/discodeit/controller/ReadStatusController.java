package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // ✅ 읽음 상태 생성
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody ReadStatusCreateRequest request) {
        UUID createdId = readStatusService.create(request);
        return ResponseEntity.status(201).body(createdId);
    }

    // ✅ 사용자별 읽음 상태 목록 조회
    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> findAllByUser(@RequestParam UUID userId) {
        List<ReadStatusDto> statuses = readStatusService.findAllByUserId(userId).stream()
                .map(rs -> new ReadStatusDto(
                        rs.getId(),
                        rs.getUserId(),
                        rs.getChannelId(),
                        rs.getLastReadAt(),
                        rs.getCreatedAt(),
                        rs.getUpdatedAt()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    // ✅ 읽음 상태 수정 (반환 타입 수정 완료)
    @PatchMapping
    public ResponseEntity<ReadStatusDto> update(@RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updated = readStatusService.update(request);

        ReadStatusDto dto = new ReadStatusDto(
                updated.getId(),
                updated.getUserId(),
                updated.getChannelId(),
                updated.getLastReadAt(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );

        return ResponseEntity.ok(dto);
    }
}
