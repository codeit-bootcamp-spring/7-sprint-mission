package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateByUserRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/userStatus")
@RequiredArgsConstructor
public class UserStatusController {
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<UserStatusResponse> create(@Valid @RequestBody UserStatusCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userStatusService.create(request));
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<UserStatusResponse> updateOnlineStatus(@Valid @RequestBody UserStatusUpdateByUserRequest request) {
        return ResponseEntity.ok(userStatusService.updateByUser(request));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserStatusResponse>> getAll() {
        return ResponseEntity.ok(userStatusService.getAll());
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<UserStatusResponse> get(@RequestParam UUID userUuid) {
        return ResponseEntity.ok(userStatusService.getByUser(userUuid));
    }
}
