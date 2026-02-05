package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 등록 (/users)
    // 200 반환
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDto> userCreate(
            @Valid @RequestPart("userCreateRequest") UserCreateRequest userRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ) {
        UserDto userDto = userService.createUser(userRequestDto, profileImage);
        return ResponseEntity.ok(userDto);
    }

    // 사용자 전체 조회 (/users)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // 사용자 단일 조회 (/user/id)
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    // 사용자 삭제 (/user/userId)
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> userDelete(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 사용자 정보 수정 (/user/..)
    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<UserDto> userUpdate(
            @PathVariable UUID userId,
            @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateDto,
            @RequestPart(value = "profile",  required = false) MultipartFile profileImage
    ) {
        return ResponseEntity.ok(userService.updateUserInfo(userId,userUpdateDto,profileImage));
    }
}
