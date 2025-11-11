package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestPart(value = "userDto") CreateUserDto userDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
        CreateBinaryContentDto createBinaryContentDto = null;

        if (profile != null && !profile.isEmpty()) {
            createBinaryContentDto = new CreateBinaryContentDto(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getSize(),
                    profile.getBytes()
            );
        }

        UserResponseDto userResponseDto = userService.createUser(userDto, createBinaryContentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserDto updateUserDto) {
        UserResponseDto userResponseDto = userService.updateUser(userId, updateUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
    public ResponseEntity<UserStatusResponseDto> updateUserState(@PathVariable UUID userId,
                                             @RequestBody UpdateUserStatusDto updateUserDto) {
        UserStatusResponseDto userStatusResponseDto = userStatusService.updateStatusByUserId(userId, updateUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(userStatusResponseDto);
    }

}
