package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /** 사용자 등록 (JSON) */
  @RequestMapping(method = RequestMethod.POST,
      consumes = "application/json",
      produces = "application/json")
  public ResponseEntity<Map<String, UUID>> create(@RequestBody UserCreateRequest req) {
    Assert.hasText(req.username(), "유저 이름을 정확히 입력해주세요.");
    Assert.hasText(req.email(), "이메일을 정확히 입력해주세요.");
    Assert.hasText(req.password(), "비밀번호를 다시 확인해주세요.");

    var created = userService.create(req, Optional.empty());
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Map.of("id", created.getId()));
  }

  /** 사용자 단건 조회 */
  @RequestMapping(value = "/{id}",
      method = RequestMethod.GET,
      produces = "application/json")
  public ResponseEntity<UserDto> find(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.find(id));
  }

  /** 모든 사용자 조회 */
  @RequestMapping(method = RequestMethod.GET,
      produces = "application/json")
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  /** 사용자 정보 수정 (스펙에 맞게 PATCH 사용) */
  @RequestMapping(value = "/{id}",
      method = RequestMethod.PATCH,
      consumes = "application/json",
      produces = "application/json")
  public ResponseEntity<Map<String, UUID>> update(
      @PathVariable UUID id,
      @RequestBody UserUpdateRequest req
  ) {
    Assert.notNull(id, "userId must not be null");
    var updated = userService.update(id, req, Optional.empty());
    return ResponseEntity.ok(Map.of("id", updated.getId()));
  }

  /** 사용자 삭제 */
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    Assert.notNull(id, "userId must not be null");
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /** 사용자 상태(온라인 등) 업데이트 - 추후 구현 */
  @RequestMapping(value = "/{id}/userStatus",
      method = RequestMethod.PATCH,
      consumes = "application/json")
  public ResponseEntity<Void> updateOnline(
      @PathVariable UUID id,
      @RequestBody Object req
  ) {
    throw new UnsupportedOperationException("User status update service not wired yet");
  }
}