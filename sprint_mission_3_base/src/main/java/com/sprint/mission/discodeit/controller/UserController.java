package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 사용자 생성
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserCreateRequest request) {
        UserDto createdUser = userService.create(request);
        return ResponseEntity.status(201).body(createdUser); // 생성 시 201 Created
    }

    // ✅ 사용자 전체 조회
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
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
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody Map<String, Object> updates) {
        try {
            // username이나 email 중 들어온 것만 업데이트
            if (updates.containsKey("username")) {
                String newUsername = (String) updates.get("username");
                userService.updateUsername(userId, newUsername);
            }
            if (updates.containsKey("email")) {
                String newEmail = (String) updates.get("email");
                userService.updateEmail(userId, newEmail);
            }

            return ResponseEntity.ok().body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }
}
