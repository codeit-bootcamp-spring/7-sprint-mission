package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.dto.entity.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> signIn(@Valid @RequestPart UserCreateRequest userCreateRequest, @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signIn(userCreateRequest, profile));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> update(@PathVariable UUID userId, @Valid @RequestPart(name = "userUpdateRequest") UserUpdateRequest userUpdateRequest, @RequestPart(name = "profile", required = false) MultipartFile profile) {
        return ResponseEntity.ok(userService.update(userId, userUpdateRequest, profile));
    }
}
