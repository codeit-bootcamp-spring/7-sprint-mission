package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor // 의존성 주입을 생성자로 처리
public class UserController {

    private final UserService userService;

    /** 사용자 등록 */
    @RequestMapping(value = "/api/users", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, UUID>> create(@RequestBody UserCreateRequest req) {
        Assert.hasText(req.username(), "username must not be empty");
        Assert.hasText(req.email(), "email must not be empty");
        Assert.hasText(req.password(), "password must not be empty");
        var created = userService.create(req, Optional.empty());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", created.getId()));
    }

    /** 사용자 단건 조회 */
    @RequestMapping(value = "/api/users", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<UserDto> find(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.find(id));
    }

    /** 모든 사용자 조회 */
    @RequestMapping(value = "/api/users", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /** 사용자 정보 수정 */
    @RequestMapping(value = "/api/users", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, UUID>> update(@PathVariable UUID id, @RequestBody UserUpdateRequest req) {
        Assert.notNull(id, "userId must not be null");
        var updated = userService.update(id, req, Optional.empty());
        return ResponseEntity.ok(Map.of("id", updated.getId()));
    }

    /** 사용자 삭제 */
    @RequestMapping(value = "/api/users", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        Assert.notNull(id, "userId must not be null");
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** 온라인 상태 업데이트(서비스 메서드 확정 시 연결) */
    @RequestMapping(value = "/api/users/online", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<Void> updateOnline(@PathVariable UUID id, @RequestBody Object req) {
        throw new UnsupportedOperationException("User online update service not wired yet");
    }
}