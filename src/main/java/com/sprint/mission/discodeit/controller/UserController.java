package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicUserService;
import com.sprint.mission.discodeit.application.dto.request.ProfileRequest;
import com.sprint.mission.discodeit.application.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.application.dto.response.UserResponse;
import com.sprint.mission.discodeit.application.dto.response.UserStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final BasicUserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponse createUser(@ModelAttribute UserCreateRequest userCreateRequest,
                                   @ModelAttribute ProfileRequest profile) {
        return userService.createUser(userCreateRequest, profile);
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(
            @PathVariable UUID userId,
            @ModelAttribute UserUpdateReq userUpdateRequest,
            @ModelAttribute ProfileRequest profile){
        UserResponse userResponse = userService.updateUserInfo(userId, userUpdateRequest, profile);
        return userResponse;
    }


    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return "삭제완료";
    }

    @PatchMapping("/{userId}/userStatus")
    public UserStatusResponse markOnline(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request) {
        return userService.markUserStatus(userId, request.newLastActiveAt());
    }

}
