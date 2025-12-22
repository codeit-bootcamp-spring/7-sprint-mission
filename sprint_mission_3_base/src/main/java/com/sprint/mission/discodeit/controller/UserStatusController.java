package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
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

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserStatusDto> getByUserId(@PathVariable UUID userId) {
        UserStatusDto status = userStatusService.getByUserId(userId);
        return ResponseEntity.ok(status);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserStatusDto> updateByUserId(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequest request
    ) {
        UserStatusDto updated =
                userStatusService.updateByUserId(userId, request);

        return ResponseEntity.ok(updated);
    }
}
