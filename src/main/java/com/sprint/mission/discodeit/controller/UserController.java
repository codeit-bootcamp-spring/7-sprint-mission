package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.global.util.ApiResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 사용자 관리
 * [ ] 사용자를 등록할 수 있다.                      api/users, Post
 * [ ] 사용자 정보를 수정할 수 있다.                 api/users/{userId}, Put
 * [ ] 사용자를 삭제할 수 있다.                      api/users, Delete
 * [ ] 모든 사용자를 조회할 수 있다.                 api/users, Get
 * [ ] 사용자의 온라인 상태를 업데이트할 수 있다.    api/users/status/{userId}, Put
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestPart(value = "userDto") CreateUserDto userDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
        CreateBinaryContentDto createBinaryContentDto = null;

        if (profile != null && !profile.isEmpty()) {
            createBinaryContentDto = new CreateBinaryContentDto(
                    profile.getOriginalFilename(),  // ✅ 실제 업로드된 파일 이름
                    profile.getContentType(),       // ✅ Content-Type (예: image/jpeg)
                    profile.getBytes()               // ✅ 파일 데이터
            );
        }

        UserResponseDto userResponseDto = userService.createUser(userDto, createBinaryContentDto);
        ApiResponse<UserResponseDto> responseBody = ApiResponse.success(userResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUser() {
        List<UserResponseDto> users = userService.getAllUsers();
        ApiResponse<List<UserResponseDto>> responseBody = ApiResponse.success(users);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserDto updateUserDto) {
        UserResponseDto userResponseDto = userService.updateUser(userId, updateUserDto);
        ApiResponse<UserResponseDto> responseBody = ApiResponse.success(userResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @RequestMapping(value = "/status/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserState(@PathVariable UUID userId,
                                             @RequestBody UpdateUserStatusDto updateUserDto) {
        UserStatusResponseDto userStatusResponseDto = userStatusService.updateStatusByUserId(userId, updateUserDto);
        ApiResponse<UserStatusResponseDto> responseBody = ApiResponse.success(userStatusResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


}
