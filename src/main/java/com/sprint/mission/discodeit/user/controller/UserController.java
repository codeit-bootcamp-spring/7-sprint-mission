package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.application.dto.ProfileUpdateResponse;
import com.sprint.mission.discodeit.application.dto.UserDetailInfo;
import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.*;
import com.sprint.mission.discodeit.user.state.dto.UserStatusRequestDTO;
import com.sprint.mission.discodeit.user.state.dto.UserStatusResponse;
import com.sprint.mission.discodeit.user.state.dto.UserStatusResponseDTO;
import com.sprint.mission.discodeit.user.state.UserStatusService;
import com.sprint.mission.discodeit.user.state.dto.UserStatusUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserManagementService userManagementService;
  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping
  public ResponseEntity<UserResponseDTO> register(
      @RequestPart(value = "userCreateRequest") UserRequestDTO requestDTO,
      @RequestPart(value = "profile", required = false) MultipartFile multipartFile
  ) {
    UserResponseDTO responseDTO = userManagementService.createUserWithRelatedData(requestDTO,
        multipartFile);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable("userId") UUID userId
//      @RequestAttribute("userId") String userIdString
  ) {
//    UUID userId = UUID.fromString(userIdString);
    userManagementService.deleteUserWithRelatedData(userId);
    return ResponseEntity.ok("성공적으로 회원탈퇴 되었습니다.");
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<ProfileUpdateResponse> updateProfile(@PathVariable("userId") UUID userId,
//      @RequestAttribute("userId") String userIdString,
      @RequestPart(value = "userRequest") UserUpdateRequest requestDTO,
      @RequestPart(value = "profile", required = false) MultipartFile multipartFile) {
//    UUID userId = UUID.fromString(userIdString);

    return ResponseEntity.ok(
        userManagementService.updateProfile(userId, requestDTO, multipartFile));
  }

  @PatchMapping("/changePassword/{userId}")
  public ResponseEntity<String> changePassword(@PathVariable("userId") UUID userId,
//      @RequestAttribute("userId") String userIdString,
      @RequestBody String newPassword) {
//    UUID userId = UUID.fromString(userIdString);
    userService.changePassword(userId, newPassword);
    return ResponseEntity.ok("성공적으로 비밀번호가 변경되었습니다.");
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    List<UserResponseDTO> users = userService.findAll().stream()
        .map(UserResponseDTO::fromEntity)
        .toList();
    return ResponseEntity.ok(users);
  }

  @PutMapping("/changeusername/{userId}")
  public ResponseEntity<?> changeUserName(
      @PathVariable("userId") UUID userId,
//      @RequestAttribute("userId") String userIdString,
      @RequestBody @Valid ChangeUserNameRequest request) {
    User user = userService.findById(userId);
    UserResponseDTO responseDTO = userService.changeUserName(user.getId(), request.username());
    return ResponseEntity.ok(responseDTO);
  }

  @GetMapping("/details/{userId}")
  public ResponseEntity<UserDetailInfo> getUserDetailInfo(
      @PathVariable("userId") UUID userId
//      @RequestAttribute("userId") String userIdString
  ) {
//    UUID userId = UUID.fromString(userIdString);
    UserDetailInfo detailInfoDTO = userManagementService.getUserDetailInfo(userId);
    return ResponseEntity.ok(detailInfoDTO);
  }

  @PatchMapping("/status")
  public ResponseEntity<?> updateUserStatus(
      @RequestAttribute("userId") String userIdString,
      @RequestBody UserStatusRequestDTO requestDTO) {

    UUID userId = UUID.fromString(userIdString);
    UserStatusResponseDTO responseDTO = processUserStatusUpdate(userId, requestDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<?> updateUserStatus(@PathVariable("userId") UUID userId,
      UserStatusUpdateRequest requestDTO) {
    UserStatusResponse response = userStatusService.updateLastOnline(userId, requestDTO);
    return ResponseEntity.ok(response);
  }

  private UserStatusResponseDTO processUserStatusUpdate(UUID userId,
      UserStatusRequestDTO requestDTO) {

    return switch (requestDTO.status()) {
      case ONLINE -> userStatusService.toOnline(userId);
      case OFFLINE -> userStatusService.toOffline(userId);
      case AFK -> userStatusService.toAway(userId);
      case DND -> userStatusService.toDoNotDisturb(userId, requestDTO.message());
      default -> throw new IllegalArgumentException("확인 할 수 없는 상태값입니다: " + requestDTO.status());
    };
  }

}


