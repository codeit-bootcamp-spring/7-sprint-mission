package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 사용자 생성
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody com.sprint.mission.discodeit.dto.user.UserCreateRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    // ✅ 사용자 전체 조회 (심화 요구사항)
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // ✅ 사용자 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> find(@PathVariable UUID id) {
        return userService.find(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
