package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/status")
public class UserStatusController {

    private final UserStatusService userStatusService;
    private final UserService userService;

    public UserStatusController(UserStatusService userStatusService, UserService userService) {
        this.userStatusService = userStatusService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateStatus(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequest request
            ) {

        UserStatus updated = userStatusService.updateByUserId(userId, request);

        UserDto user = userService.find(userId);

        return ResponseEntity.ok(user);
    }
}
