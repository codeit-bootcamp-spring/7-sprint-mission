package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest; // ✅ 추가
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.Instant;


@RestController
@RequestMapping("/api/user-status")
public class UserStatusController {


    private final UserStatusService userStatusService;


    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }


    @RequestMapping(method = RequestMethod.POST)
    public UUID create(@RequestBody UserStatusCreateRequest request) {
        return userStatusService.create(request);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Instant findLastSeen(@PathVariable UUID userId) {
        return userStatusService.findLastSeenByUserId(userId).orElse(null);
    }


    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody UserStatusUpdateByUserIdRequest request) {
        userStatusService.updateByUserId(request.userId(), request.lastSeenAt());
    }
}