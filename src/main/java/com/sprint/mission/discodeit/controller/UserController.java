package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.UserApi;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestPart("userCreateRequest") UserSignupRequestDto userSignupRequestDto,
                                                      @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {

        log.info("회원가입 요청 - hasProfile={}", profile != null);
        UserSignupCommand command = UserSignupCommand.from(userSignupRequestDto, profile);
        UserResponseDto responseDto = userService.signUp(command);
        log.info("회원가입 성공 - userId={}", responseDto.id());

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestPart("userUpdateRequest") UserUpdateRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {

        log.debug("회원 수정 요청 - userId={}, hasProfile={}", userId, profile != null);
        UserUpdateCommand command = UserUpdateCommand.from(userId, request, profile);
        UserResponseDto userResponseDto = userService.updateUser(command);
        log.info("회원 수정 성공 - userId={}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        log.debug("회원 목록 조회 요청");
        List<UserResponseDto> allUsers = userService.getAllUsers();
        log.debug("회원 목록 조회 성공 - count={}", allUsers.size());

        return ResponseEntity.ok(allUsers);
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {

        log.info("회원 삭제 요청 - userId={}", userId);
        userService.deleteUser(userId);
        log.info("회원 삭제 성공 - userId={}", userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
