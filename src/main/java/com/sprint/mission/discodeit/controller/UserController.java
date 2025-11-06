package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestPart("request") CreateUserRequestDto requestDto,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        UserResponseDto newUser = userService.create(requestDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // 사용자 정보 수정
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateUser(@PathVariable UUID userId,
                                             @RequestPart(name = "request", required = false) UpdateUserRequestDto requestDto,
                                             @RequestPart(name = "file", required = false) MultipartFile file) {
        userService.update(userId, requestDto, file);
        return ResponseEntity.ok().body("User Updated Successfully");
    }

    // 사용자 삭제
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok().body("User Deleted Successfully");
    }

    // 모든 사용자 조회
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> searchUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    // 사용자 온라인 업데이트
    @RequestMapping(value = "/users/{userId}/online", method = RequestMethod.POST)
    public ResponseEntity<String> onlineUser(@PathVariable UUID userId) {
        userStatusService.updateByUserId(userId);
        return ResponseEntity.ok().body("User Online Successfully");
    }
}
