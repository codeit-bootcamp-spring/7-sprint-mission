package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /** 사용자 등록 (multipart/form-data: userCreateRequest JSON + profile 파일) */
  @RequestMapping(
      method = org.springframework.web.bind.annotation.RequestMethod.POST,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, UUID>> create(
      @RequestPart("userCreateRequest") UserCreateRequest req,
      @RequestPart(name = "profile", required = false) MultipartFile profile
  ) {
    Assert.hasText(req.username(), "유저 이름을 정확히 입력해주세요.");
    Assert.hasText(req.email(), "이메일을 정확히 입력해주세요.");
    Assert.hasText(req.password(), "비밀번호를 다시 확인해주세요.");

    Optional<BinaryContentCreateRequest> profileReq = toBinaryContentCreateRequest(profile);
    var created = userService.create(req, profileReq);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", created.getId()));
  }

  /** 사용자 단건 조회 */
  @RequestMapping(
      value = "/{userId}",
      method = org.springframework.web.bind.annotation.RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserDto> find(@PathVariable("userId") UUID userId) {
    return ResponseEntity.ok(userService.find(userId));
  }

  /** 모든 사용자 조회 */
  @RequestMapping(
      method = org.springframework.web.bind.annotation.RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  /** 사용자 정보 수정 (multipart/form-data: userUpdateRequest JSON + profile 파일) */
  @RequestMapping(
      value = "/{userId}",
      method = org.springframework.web.bind.annotation.RequestMethod.PATCH,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, UUID>> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest req,
      @RequestPart(name = "profile", required = false) MultipartFile profile
  ) {
    Assert.notNull(userId, "유저ID가 null일 수 없습니다.");
    Optional<BinaryContentCreateRequest> profileReq = toBinaryContentCreateRequest(profile);
    var updated = userService.update(userId, req, profileReq);
    return ResponseEntity.ok(Map.of("id", updated.getId()));
  }

  /** 사용자 삭제 */
  @RequestMapping(
      value = "/{userId}",
      method = org.springframework.web.bind.annotation.RequestMethod.DELETE
  )
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    Assert.notNull(userId, "userId must not be null");
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  /** 사용자 상태(온라인/오프라인 등) 업데이트 */
  @RequestMapping(
      value = "/{userId}/userStatus",
      method = org.springframework.web.bind.annotation.RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> updateStatus(
      @PathVariable("userId") UUID userId,
      @RequestBody Map<String, Object> statusReq
  ) {
    Assert.notNull(userId, "userId must not be null");
    Assert.notEmpty(statusReq, "요청 데이터가 비어 있습니다.");

    userService.updateStatus(userId, statusReq);
    return ResponseEntity.ok().build();
  }

  /**
   * 프로필 이미지 업로드용 MultipartFile을 내부에서 사용하는 BinaryContentCreateRequest로 변환한다.
   * - 컨트롤러는 HTTP 요청으로 들어온 MultipartFile만 알고,
   * - 서비스는 파일 메타데이터/바이트를 가진 BinaryContentCreateRequest만 알도록
   *   두 계층 사이를 연결해 주는 변환 메서드이다.
   *
   * 파일이 null 이거나 비어 있으면 프로필을 설정하지 않은 것으로 보고 Optional.empty()를 반환하고,
   * 파일을 읽는 중 오류가 발생하면 IllegalStateException으로 감싸서 던진다.
   */
  // “프로필 이미지 MultipartFile을 서비스에서 사용하는 BinaryContentCreateRequest로 바꿔주는 메서드. 파일이 없으면 Optional.empty()를 돌려준다.”
  private Optional<BinaryContentCreateRequest> toBinaryContentCreateRequest(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return Optional.empty();  // 파일이 없거나 비었으면 > Optional.empty() 반환
    }
    try {
      return Optional.of(new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    } catch (IOException e) {
      throw new IllegalStateException("프로필을 읽는 중 오류가 발생했습니다.", e);
    }
  }
}