package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 사용자 관리
 * [ ] 사용자를 등록할 수 있다.                      api/user, Post
 * [ ] 사용자 정보를 수정할 수 있다.                 api/user/{userId}, Put
 * [ ] 사용자를 삭제할 수 있다.                      api/user, Delete
 * [ ] 모든 사용자를 조회할 수 있다.                 api/user, Get
 * [ ] 사용자의 온라인 상태를 업데이트할 수 있다.    api/user/status/{userId}, Put
 */
@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto userDto) {
        UserResponseDto userResponseDto = userService.createUser(userDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserDto updateUserDto) {
        UserResponseDto userResponseDto = userService.updateUser(userId, updateUserDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @RequestMapping(value = "/status/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserState(@PathVariable UUID userId,
                                             @RequestBody UpdateUserStatusDto updateUserDto) {
        UserStatus userStatus = userStatusService.updateStatusByUserId(userId, updateUserDto);
        return ResponseEntity.ok(userStatus);
    }


}
