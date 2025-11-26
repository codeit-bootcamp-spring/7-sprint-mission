package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.UserControllerDocs;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;
    private final UserStatusService userStatusService;

    private final BinaryContentMapper binaryContentMapper;

    // 사용자 등록
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestPart("userCreateRequest") CreateUserRequestDto requestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        CreateBinaryContentRequestDto profileRequestDto = binaryContentMapper.toRequestDto(profile);
        UserResponseDto createdUser = userService.create(requestDto, profileRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // 사용자 정보 수정
    @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable UUID userId,
            @RequestPart(name = "userUpdateRequest", required = false) UpdateUserRequestDto requestDto,
            @RequestPart(name = "profile", required = false) MultipartFile profile
    ) {
        CreateBinaryContentRequestDto profileRequestDto = binaryContentMapper.toRequestDto(profile);
        UserResponseDto updatedUser = userService.update(userId, requestDto, profileRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    // 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> searchUsers() {
        List<UserResponseDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // 사용자 온라인 업데이트
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResponseDto> onlineUser(
            @PathVariable UUID userId,
            @RequestBody UpdateUserStatusRequestDto request
    ) {
        UserStatusResponseDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUserStatus);
    }
}
