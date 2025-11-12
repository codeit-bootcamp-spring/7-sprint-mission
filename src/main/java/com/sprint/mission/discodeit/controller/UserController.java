package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.dto.ApiResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @RequestMapping(
            value = "/users",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<User> createUser(
            @RequestPart("userCreateRequest") CreateUserRequestDto requestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        CreateBinaryContentRequestDto profileRequestDto = convertToRequestDto(profile);
        User createdUser = userService.create(requestDto, profileRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // 사용자 정보 수정
    @RequestMapping(
            value = "/users/{userId}",
            method = RequestMethod.PATCH,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<User> updateUser(
            @PathVariable UUID userId,
            @RequestPart(name = "userUpdateRequest", required = false) UpdateUserRequestDto requestDto,
            @RequestPart(name = "profile", required = false) MultipartFile profile
    ) {
        CreateBinaryContentRequestDto profileRequestDto = convertToRequestDto(profile);
        User updatedUser = userService.update(userId, requestDto, profileRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    // 사용자 삭제
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 모든 사용자 조회
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> searchUsers() {
        List<UserResponseDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // 사용자 온라인 업데이트
    @RequestMapping(value = "/users/{userId}/userStatus", method = RequestMethod.POST)
    public ResponseEntity<UserStatus> onlineUser(
            @PathVariable UUID userId,
            @RequestBody UpdateUserStatusRequestDto request
    ) {
        UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUserStatus);
    }

    private CreateBinaryContentRequestDto convertToRequestDto(MultipartFile file) {
        CreateBinaryContentRequestDto profileRequestDto = null;

        if(file != null || !file.isEmpty()) {
            try {
                profileRequestDto = new CreateBinaryContentRequestDto(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return profileRequestDto;
    }
}
