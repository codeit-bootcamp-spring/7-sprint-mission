package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*
사용자 관리
[ ] 사용자를 등록할 수 있다. /user, Post
[ ] 사용자 정보를 수정할 수 있다. /user, PUt
[ ] 사용자를 삭제할 수 있다. /user, Delete
[ ] 모든 사용자를 조회할 수 있다. /user, /Get
[ ] 사용자의 온라인 상태를 업데이트할 수 있다. /user/{id}/
 */
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto userDto) {

        User user = userService.createUser(userDto);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/{userId}/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserState(@PathVariable UUID userId,
            @RequestBody UpdateUserStatusDto updateUserDto) {
        UserStatus userStatus = userStatusService.updateUserStatusByUserId(userId, updateUserDto);
        return ResponseEntity.ok(userStatus);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@RequestBody UUID userId) {

    }

}
