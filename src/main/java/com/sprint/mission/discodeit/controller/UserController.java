package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.dto.response.userStatus.UserStatusDto;
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
public class UserController{

    private final UserService userService;
    private final UserStatusService userStatusService;

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable UUID userId){
        userService.deleteUser(userId);
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> readAll(){
        return new ResponseEntity<>( userService.findAllUsers(),HttpStatus.OK);
    }

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestPart("userCreateRequest") UserCreateRequestDto dto
            , @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        return new ResponseEntity<UserDto>(userService.createUser(dto,profile),HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> patchUser(@PathVariable UUID userId,
                                             @Valid @RequestPart("userUpdateRequest") UserUpdateRequest dto,
                                             @RequestPart(value = "profile", required = false) MultipartFile profile)   {
       ;
        return new ResponseEntity<UserDto>(userService.patchUser(userId, dto,profile), HttpStatus.OK);
    }

    @PatchMapping(value = "/{userId}/userStatus")
    public ResponseEntity<UserStatusDto> patchUserStatus(@PathVariable UUID userId, @Valid @RequestBody UserStatusPatchRequestDto dto){
;
        return new ResponseEntity<UserStatusDto>(userStatusService.patchUserStatus(userId, dto), HttpStatus.OK);
    }

    @PostMapping("/reset")
    public void reset(){
        userService.resetUserRepository();
    }

    @GetMapping(value = "/userStatus")
    public ResponseEntity<List<UserStatusDto>> readAllUserStatus(){
        return new ResponseEntity<>(userStatusService.findAll(),HttpStatus.OK);
    }
}
