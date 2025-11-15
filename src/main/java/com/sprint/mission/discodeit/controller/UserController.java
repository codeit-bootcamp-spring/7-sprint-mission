package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.UserControllerDocs;
import com.sprint.mission.discodeit.service.BasicUserService;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.dto.response.UserStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final BasicUserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
                                     @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        UserResponse response = userService.createUser(userCreateRequest, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile",required = false) MultipartFile profile) {
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
        return userService.markOnline(userId, request.newLastActiveAt());
    }

}
