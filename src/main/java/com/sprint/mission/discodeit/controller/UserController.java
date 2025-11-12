package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.userDto.UserRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserResponseDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final UserStatusService userStatusService;

    // 사용자 등록 (/users)
    // 201 반환
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> userCreate(
            @Valid @RequestPart("userRequestDto") UserRequestDto userRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userRequestDto, profileImage));
    }

    // 사용자 전체 조회 (/users)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // 사용자 단일 조회 (/user/id)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    // 사용자 삭제 (/user/id)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> userDelete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();      // 204 no content
        // return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");     // 200 ok
        // return ResponseEntity.status(HttpStatus.OK).body("사용자가 성공적으로 삭제되었습니다."); // 반환타입이 String
        // 실패 시 예외를 알아서 처리
    }

    // 사용자 정보 수정 (/user/..)
    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<User> userUpdate(
            @PathVariable UUID userId,
            @Valid @RequestPart("UserUpdateDto") UserUpdateDto userUpdateDto,
            @RequestPart(value = "profile",  required = false) MultipartFile profileImage
    ) {
        return ResponseEntity.ok(userService.updateUserInfo(userId,userUpdateDto,profileImage));
    }

    // 사용자 온라인 상태 업데이트  (/users/userId/online)
    @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PUT)
    public ResponseEntity<UserStatusResponseDto> onlineUpdate(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateDto updateDto) {
        return ResponseEntity.ok(userStatusService.updateStatusByUserId(userId));
    }
}
/*
    사용자 관리
    [ ] 사용자를 등록할 수 있다.
    [ ] 사용자 정보를 수정할 수 있다.
    [ ] 사용자를 삭제할 수 있다.
    [ ] 모든 사용자를 조회할 수 있다.
    [ ] 사용자의 온라인 상태를 업데이트할 수 있다.

    HTTP 메소드
    GET : 리소스를 조회
    POST : 데이터 추가, 등록
    PUT : 리소스 대체, 수정 / 해당 리소스가 없으면 새롭게 생성
    DELETE : 리소스 삭제
    PATCH : 리소스 부분 변경(수정)

    @PathVariable 경로 변수
    @RequestParam 쿼리 파라미터
    @RequestBody 요청본문
    @Valid 유효성 검 -> @RequestBody 바로 앞에 붙여서 DTO를 검증
     */
