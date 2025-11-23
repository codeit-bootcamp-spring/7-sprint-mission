package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserInfoReq;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.facade.user.UserCreationFacade;
import com.sprint.mission.discodeit.facade.user.UserDeletionFacade;
import com.sprint.mission.discodeit.facade.user.UserDetailViewFacade;
import com.sprint.mission.discodeit.facade.user.UserOverviewFacade;
import com.sprint.mission.discodeit.facade.user.UserUpdateFacade;
import com.sprint.mission.discodeit.facade.userstatus.UserStatusUpdateFacade;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

  private final UserCreationFacade userCreationFacade;
  private final UserOverviewFacade userOverviewFacade;
  private final UserDetailViewFacade userDetailViewFacade;
  private final UserUpdateFacade userUpdateFacade;
  private final UserDeletionFacade userDeletionFacade;
  private final UserStatusUpdateFacade userStatusUpdateFacade;
  private final BinaryContentService binaryContentService;
  private final UserService userService;

  //사용자 목록 조회
  @GetMapping
  public ResponseEntity<List<UserSimpleInfoRes>> getAllUsers() {
    return ResponseEntity.ok(userOverviewFacade.findAll());
  }

  //사용자 단일 조회
  @GetMapping("/{userId}")
  public ResponseEntity<UserDetailInfoRes> getUserByUserId(@PathVariable UUID userId) {
    return ResponseEntity.ok(userDetailViewFacade.findById(userId));
  }

  //회원 등록
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDetailInfoRes> createUser(
      @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
    BinaryContentCreateReq binaryContentCreateReq = BinaryContentCreateReq.from(profileFile);
    UserDetailInfoRes user = userCreationFacade.createUser(
        UserCreateReq.from(userInfoReq, binaryContentCreateReq));
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand(user.userId())
        .toUri();
    return ResponseEntity.created(location).body(user);
  }

  // 회원 수정
  @PutMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDetailInfoRes> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    BinaryContentCreateReq binaryContentCreateReq = BinaryContentCreateReq.from(profile);
    UserUpdateReq req = UserUpdateReq.from(userInfoReq, binaryContentCreateReq);
    UserDetailInfoRes res = userUpdateFacade.updateUser(userId, req);
    return ResponseEntity.ok(res);
  }

  // 회원 삭제
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userDeletionFacade.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  // 유저 온라인 상태 업그레이드
  @PatchMapping("/{userId}/status")
  public ResponseEntity<UserStatusSimpleViewRes> updateUserStatus(@PathVariable UUID userId) {
    return ResponseEntity.ok(userStatusUpdateFacade.update(userId));
  }

  //메일로 가입했던 아이디 발송
  @GetMapping("/id")
  public ResponseEntity<Void> sendEmailId(@RequestParam String email) {
    userService.sendEmailId(email);
    return ResponseEntity.noContent().build();
  }
}
