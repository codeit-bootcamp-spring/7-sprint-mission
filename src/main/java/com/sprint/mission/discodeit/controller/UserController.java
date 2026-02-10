package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.doc.UserDocs;
import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController implements UserDocs {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestPart(value = "userCreateRequest") CreateUserDto userDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

        Optional<CreateBinaryContentDto> createBinaryContentDto = CreateBinaryContentDto.of(profile);
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

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID userId,
            @RequestPart(value = "userUpdateRequest") UpdateUserDto updateUserDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

        Optional<CreateBinaryContentDto> createBinaryContentDto = CreateBinaryContentDto.of(profile);
        UserResponseDto createUserResponseDto = userService.updateUser(userId, updateUserDto,
                createBinaryContentDto);
        return ResponseEntity.status(HttpStatus.OK).body(createUserResponseDto);
    }

}
