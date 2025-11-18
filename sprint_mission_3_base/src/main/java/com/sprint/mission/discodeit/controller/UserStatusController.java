package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-statuses")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    // ✅ 사용자 상태 생성
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody UserStatusCreateRequest request) {
        UUID created = userStatusService.create(request);
        return ResponseEntity.status(201).body(created);
    }

    // ✅ 사용자 마지막 접속 시간 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Instant> findLastSeen(@PathVariable UUID userId) {
        return userStatusService.findLastSeenByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 사용자 상태 업데이트 (예: 마지막 접속 시간 갱신)
    @PatchMapping
    public ResponseEntity<UserStatusDto> update(@RequestBody UserStatusUpdateByUserIdRequest request) {
        UserStatusDto updated = userStatusService.updateByUserId(request.userId(), request.lastSeenAt());
        return ResponseEntity.ok(updated);
    }
}
