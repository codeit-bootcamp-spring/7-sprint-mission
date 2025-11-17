package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserStatusController {

    private final UserStatusService userStatusService;

    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @PatchMapping("/{id}/userStatus")
    public ResponseEntity<UserStatusDto> updateStatus(
            @PathVariable("id") UUID userId,
            @RequestBody UserStatusUpdateRequest request
            ) {

        UserStatus updated = userStatusService.updateByUserId(userId, request);

        return ResponseEntity.ok(UserStatusDto.from(updated));
    }
}
