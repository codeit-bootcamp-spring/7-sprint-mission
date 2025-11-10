package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestPart("request") CreateUserRequestDto requestDto,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        userService.create(requestDto, file);
        return ApiResponse.success(HttpStatus.CREATED, "사용자 등록에 성공했습니다.");
    }

    // 사용자 정보 수정
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<Object>> updateUser(@PathVariable UUID userId,
                                             @RequestPart(name = "request", required = false) UpdateUserRequestDto requestDto,
                                             @RequestPart(name = "file", required = false) MultipartFile file) {
        userService.update(userId, requestDto, file);
        return ApiResponse.success(HttpStatus.OK, "사용자 수정에 성공했습니다.");
    }

    // 사용자 삭제
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ApiResponse.success(HttpStatus.OK, "사용자 삭제에 성공했습니다.");
    }

    // 모든 사용자 조회
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> searchUsers() {
        return ApiResponse.success(HttpStatus.OK, "모든 사용자를 조회합니다.", userService.findAll());
    }

    // 사용자 온라인 업데이트
    @RequestMapping(value = "/users/{userId}/online", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Object>> onlineUser(@PathVariable UUID userId) {
        userStatusService.updateByUserId(userId);
        return ApiResponse.success(HttpStatus.OK, "사용자 온라인 업데이트 완료.");
    }
}
