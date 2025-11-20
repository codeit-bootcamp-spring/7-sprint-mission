package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.response.UserUserStatusPatchResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @DeleteMapping("/{userId}")
    @Override
    public void delete(@PathVariable UUID userId){
        userService.deleteUser(userId);
    }

    @GetMapping("")
    @Override
    public ResponseEntity<List<UserDto>> readAll(){
        return new ResponseEntity<>( userService.advanceFindAllUser(),HttpStatus.OK);
    }

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<UserCreateResponseDto> createUser(
            @Valid @RequestPart("userCreateRequest") UserCreateRequestDto dto
            , @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        return new ResponseEntity<UserCreateResponseDto>(userService.createUser(dto,profile),HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<UserCreateResponseDto> patchUser(@PathVariable UUID userId,
                                                           @Valid @RequestPart("userUpdateRequest") UserUpdateRequest dto,
                                                           @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
       ;
        return new ResponseEntity<>(userService.patchUser(userId, dto,profile), HttpStatus.OK);
    }

    @PatchMapping(value = "/{userId}/userStatus")
    @Override
    public ResponseEntity<UserUserStatusPatchResponseDto> patchUserStatus(@PathVariable UUID userId, @Valid @RequestBody UserStatusPatchRequestDto dto){
;
        return new ResponseEntity<UserUserStatusPatchResponseDto>(userStatusService.patchUserStatus(userId, dto), HttpStatus.OK);
    }

    @PostMapping("/reset")
    public void reset(){
        userService.resetUserRepository();
    }

    @GetMapping(value = "/userStatus")
    public ResponseEntity<List<UserStatus>> readAllUserStatus(){
        return new ResponseEntity<>(userStatusService.findAll(),HttpStatus.OK);
    }
}
