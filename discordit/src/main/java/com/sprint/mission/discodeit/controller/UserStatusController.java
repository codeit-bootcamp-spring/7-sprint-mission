package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userStatus")
@RequiredArgsConstructor
public class UserStatusController {
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserStatusResponse> createOnlineStatus(@Valid @RequestBody UserStatusCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userStatusService.create(request));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<UserStatusResponse> updateOnlineStatus(@Valid @RequestBody UserStatusUpdateByUserIdRequest request) {
        return ResponseEntity.ok(userStatusService.updateByUserId(request));
    }
}
