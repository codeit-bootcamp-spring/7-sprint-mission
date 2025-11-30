package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusDto;
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
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> result = userService.getAllUsers();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
                                              @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        UserDto response = userService.createUser(userCreateRequest, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        UserDto userDto = userService.updateUserInfo(userId, userUpdateRequest, profile);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User가 성공적으로 삭제됨");
    }

    @PatchMapping("/{userId}/userStatus")
    public UserStatusDto markOnline(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request) {
        return userService.updateLastActiveAt(userId, request.newLastActiveAt());
    }

}
