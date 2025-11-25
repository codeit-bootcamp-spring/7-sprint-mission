package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userStatus")
public class UserStatusController {

    private final UserStatusService userStatusService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserStatusDto> findByUserId(@PathVariable UUID userId) {

        var status = userStatusService.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        return ResponseEntity.ok(UserStatusDto.from(status));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable UUID userId) {

        userStatusService.update(userId);

        return ResponseEntity.ok().build();
    }

}
