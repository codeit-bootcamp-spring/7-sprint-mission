package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.userDto.UserRequestDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserResponseDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserNameUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPasswordUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserPhoneNumUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userDto.userUpdate.UserStateUpdateDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록 (/users)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> userCreate(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequestDto));
    }

    // 사용자 삭제 (/user/delete?id=id)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> userDelete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();      // 204 no content
        // return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");     // 200 ok
        // return ResponseEntity.status(HttpStatus.OK).body("사용자가 성공적으로 삭제되었습니다."); // 반환타입이 String
        // 실패 시 예외를 알아서 처리
    }

    // 사용자 전체 조회 (/users)
    @RequestMapping(method = RequestMethod.GET)
    public List<UserResponseDto> getAllUser() {
        return userService.findAllUsers();
    }

    // 사용자 단일 조회 (/user/id)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserResponseDto findUserById(@PathVariable UUID id) {
        return userService.findUserInfoById(id);
    }


    // 사용자 정보 수정 (/user/update/..)
    @RequestMapping(value = "/username", method = RequestMethod.PUT)
    public UserResponseDto updateUserName(@RequestBody UserNameUpdateDto userNameUpdateDto) {
        return userService.updateUserName(userNameUpdateDto);
    }
    @RequestMapping(value = "/phonenum", method = RequestMethod.PUT)
    public UserResponseDto updatePhone(@RequestBody UserPhoneNumUpdateDto userPhoneNumUpdateDto) {
        return userService.updatePhoneNum(userPhoneNumUpdateDto);
    }
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public UserResponseDto updatePassword(@RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {
        return userService.updatePassword(userPasswordUpdateDto);
    }
    @RequestMapping(value = "/state", method = RequestMethod.PUT)
    public UserResponseDto updateState(@RequestBody UserStateUpdateDto userStateUpdateDto) {
        return userService.updateState(userStateUpdateDto);
    }

    // 사용자 온라인 상태 업데이트  (/users/userId/online)
    @RequestMapping(value = "/{userId}/online", method = RequestMethod.PUT)
    public UserStatusResponseDto updateOnline(@PathVariable UUID userId) {
        return userStatusService.updateStatusByUserId(userId);
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
