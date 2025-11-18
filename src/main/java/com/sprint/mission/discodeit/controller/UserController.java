package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserControllerDocs {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto create(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.create(userCreateRequestDto, null);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createMultipart(
            @Valid @RequestPart("userCreateRequest") UserCreateRequestDto userCreateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        UUID profileId = null;
        if(profile != null && !profile.isEmpty()) {
            BinaryContentCreateRequestDto profileDto = BinaryContentCreateRequestDto.from(profile);
            BinaryContentResponseDto binaryContentResponseDto = binaryContentService.create(profileDto);
            profileId = binaryContentResponseDto.id();
        }
        return userService.create(userCreateRequestDto, profileId);
    }

    // 사용자 정보 수정
    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto update(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                  @PathVariable("userId") UUID userId) {
        return userService.update(userId, userUpdateRequestDto, null);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponseDto updateMultipart(
            @PathVariable("userId") UUID userId,
            @Valid @RequestPart("userUpdateRequest") UserUpdateRequestDto userUpdateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        UserResponseDto userResponseDto = userService.get(userId);
        UUID profileId = userResponseDto.profileId();

        if(profile != null && !profile.isEmpty()) {
            BinaryContentCreateRequestDto profileDto
                    = BinaryContentCreateRequestDto.from(profile);
            BinaryContentResponseDto binaryContentResponseDto = binaryContentService.create(profileDto);
            UUID newProfileId = binaryContentResponseDto.id();

            if(profileId != null){
                boolean delete = binaryContentService.delete(profileId);
                if(!delete){
                    throw new NoSuchElementException("BinaryContent not found");
                }
            }

            profileId = newProfileId;
        }

        return userService.update(userId, userUpdateRequestDto, profileId);
    }

    // 사용자 삭제
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("userId") UUID userId) {
        UserResponseDto userResponseDto = userService.get(userId);
        UUID profileId = userResponseDto.profileId();

        if(profileId != null){
            binaryContentService.delete(profileId);
        }
        boolean delete = userService.delete(userId);
        if(!delete){
            log.info("User not found");
        }
    }

    // 모든 사용자 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<UserResponseDto> getAll() {
        return userService.getAll();
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> all = userService.getAll();

        return ResponseEntity.ok(all);
    }

    @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
    public UserStatusResponseDto updateUserStatusByUserId(
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto
    ) {
        return userStatusService.updateByUserId(userId, userStatusUpdateByUserIdRequestDto);
    }
}
