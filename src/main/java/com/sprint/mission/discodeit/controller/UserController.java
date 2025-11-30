package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.request.CreateUserCommand;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

  private final UserService userService;
  private final UserStatusService userStatusService;

  //PathVariable -> /api/user/{id} - 특정 자원을 식별해야할 때 사용
  //RequestParam -> /api/user?name=value - 조건(필터링, 정렬, 검색), 부가적인 데이터을 전달해야할 때(페이지 번호)
  //PATCH-> 수정에 지향한다. PUT은 자원을 교체, PATCH 자원에 일부분만 변경. 규칙상 더 효율적이고 안전함

  //사용자를 등록할 수 있다. * 해결.
  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<UserResponseDto> createUser(
      //RequestPart 파트별로 분할해서 전달
      @Valid
      @RequestPart("userCreateRequest") CreateUserRequestDto userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    UserResponseDto user = userService.createUser(userCreateRequest, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  //심화. 사용자 전체 조회 * 해결
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> findAll() {
    List<UserResponseDto> members = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(members);
  }

  //사용자 정보를 수정할 수 있다. * 해결
  @PatchMapping(value = "/{userId}", consumes = "multipart/form-data", produces = "application/json")
  public ResponseEntity<UserResponseDto> updateUser(
      @Valid @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UpdateUserDto userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    UserResponseDto user = userService.updateUser(userId, userUpdateRequest, profile);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  //사용자를 삭제할 수 있다. * 해결
  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  //사용자의 온라인 상태를 업데이트할 수 있다. * 해결
  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusResponseDto> updateUserStatusByUserId(
      @PathVariable UUID userId) {
    UserStatusResponseDto userStatus = userStatusService.updateByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(userStatus);
  }
}


