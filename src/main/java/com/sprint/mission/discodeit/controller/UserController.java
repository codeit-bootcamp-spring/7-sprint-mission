package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BinaryContentService binaryContentService;

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto create(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.create(userCreateRequestDto, null);
        boolean online = userService.isOnline(user.getId());

        return UserResponseDto.from(user, online);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponseDto createMultipart(
            @Valid @RequestPart("user") UserCreateRequestDto userCreateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        UUID profileId = null;
        if(profile != null && !profile.isEmpty()) {
            BinaryContentCreateRequestDto profileDto = BinaryContentCreateRequestDto.from(profile);
            BinaryContent binaryContent = binaryContentService.create(profileDto);
            profileId = binaryContent.getId();
        }
        User user = userService.create(userCreateRequestDto, profileId);
        boolean online = userService.isOnline(user.getId());
        return UserResponseDto.from(user, online);
    }

    // 사용자 정보 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto update(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                  @PathVariable("id") UUID id) {
        if(!id.equals(userUpdateRequestDto.id())){
            throw new IllegalArgumentException("Invalid ID");
        }
        User user = userService.update(userUpdateRequestDto, null);
        boolean online = userService.isOnline(user.getId());

        return UserResponseDto.from(user, online);
    }

    public UserResponseDto updateMultipart(
            @PathVariable("id") UUID id,
            @Valid @RequestPart("user") UserUpdateRequestDto userUpdateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        if(!id.equals(userUpdateRequestDto.id())){
            throw new IllegalArgumentException("Invalid ID");
        }

        User user = userService.get(id);
        UUID profileId = user.getProfileId();

        if(profile != null && !profile.isEmpty()) {
            BinaryContentCreateRequestDto profileDto
                    = BinaryContentCreateRequestDto.from(profile);
            BinaryContent binaryContent = binaryContentService.create(profileDto);
            UUID newProfileId = binaryContent.getId();

            if(profileId != null){
                binaryContentService.delete(profileId);
            }

            profileId = newProfileId;
        }

        User updateUser = userService.update(userUpdateRequestDto, profileId);
        boolean online = userService.isOnline(user.getId());
        return UserResponseDto.from(updateUser, online);
    }

    // 사용자 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        User user = userService.get(id);
        UUID profileId = user.getProfileId();

        if(profileId != null){
            binaryContentService.delete(profileId);
        }

        userService.delete(id);
    }

    // 모든 사용자 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<UserResponseDto> getAll() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(user ->
                        UserResponseDto.from(user,userService.isOnline(user.getId())))
                .toList();
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userService.getAll();

        List<UserResponseDto> list = users.stream()
                .map(user -> UserResponseDto.from(user, userService.isOnline(user.getId())))
                .toList();

        return ResponseEntity.ok(list);
    }
}
