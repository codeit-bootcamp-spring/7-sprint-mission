package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreateUserCommand;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserCommand;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    //사용자를 등록할 수 있다.
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserCommand request) {
        UserResponseDto user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    //사용자 단일 조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDto> findUser(@PathVariable UUID id) {
        UserResponseDto user = userService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //사용자 전체 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAllUsers() {
        List<UserResponseDto> members = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    //사용자 정보를 수정할 수 있다.
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @RequestBody UpdateUserCommand request) {
        UserResponseDto user = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //사용자를 삭제할 수 있다.
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void>deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //사용자의 온라인 상태를 업데이트할 수 있다.
    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<UserStatusResponseDto> updateOnlineStatus(@PathVariable UUID id) {
        UserStatusResponseDto userStatus = userStatusService.updateUserStatus(id);
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);
    }

}


