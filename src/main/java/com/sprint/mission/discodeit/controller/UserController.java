package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
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
    private final BinaryContentService binaryContentService;

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
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto update(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                  @PathVariable("userId") UUID userId) {
        return userService.update(userId, userUpdateRequestDto, null);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponseDto updateMultipart(
            @PathVariable("userId") UUID userId,
            @Valid @RequestPart("user") UserUpdateRequestDto userUpdateRequestDto,
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
            throw new IllegalArgumentException("User not found");
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
}
