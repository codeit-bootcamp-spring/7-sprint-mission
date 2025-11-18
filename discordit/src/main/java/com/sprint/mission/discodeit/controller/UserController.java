package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserDeleteRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.SimpleUserResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponseV2;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.DetailedUserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<SimpleUserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(SimpleUserResponse::fromUserResponseV2).toList());
    }

    @RequestMapping(value = "/detailed", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseV2>> getDetailedAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseV2> signIn(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signIn(request));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseV2> update(@PathVariable UUID userId, @Valid @RequestPart(name = "userUpdateRequest") UserUpdateRequest userUpdateRequest, @RequestPart(name = "profile", required = false) MultipartFile profile) {
        return ResponseEntity.ok(userService.update(userId, userUpdateRequest, profile));
    }

    @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
    public ResponseEntity<Instant> updateUserStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request) {
        userStatusService.updateByUser(userId, request);
        return ResponseEntity.ok(userStatusService.getByUser(userId).newLastActiveAt());
    }

    @GetMapping("/userStatus")
    public ResponseEntity<List<DetailedUserStatusResponse>> getAllUserStatus() {
        return ResponseEntity.ok(userStatusService.getAll());
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<UserResponseV2> get(@RequestParam UUID id) {

        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }


}
