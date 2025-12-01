package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;
    private final UserStatusService userStatusService;


    // [등록]

    @RequestMapping(
            method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> create(
            @RequestPart("userCreateRequest") UserCreateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);


        UserDto createUser = userService.create(request, profileRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createUser);
    }


// [수정]

    @RequestMapping(
            path = "{userId}"
            , method = RequestMethod.PATCH
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> update(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {

        Optional<BinaryContentCreateRequest> optionalProfile = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);


        UserDto update = userService.update(userId, request, optionalProfile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(update);
    }


    // [삭제]

    @RequestMapping(path = "{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
        userService.delete(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    // [전체 조회]

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAll());
    }

    @RequestMapping(path = "{userId}/userStatus", method = RequestMethod.PATCH)
    public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable UUID userId,
                                                                  @RequestBody UserStatusUpdateRequest request) {


        UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUserStatus);
    }


    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(

                        profileFile.getOriginalFilename(),
                        profileFile.getContentType(),
                        profileFile.getBytes()
                );
                return Optional.of(binaryContentCreateRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
