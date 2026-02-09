package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
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
public class UserController{

    private final UserService userService;

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
            , @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return new ResponseEntity<UserDto>(userService.createUser(dto,profile),HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> patchUser(@PathVariable UUID userId,
                                             @Valid @RequestPart("userUpdateRequest") UserUpdateRequest dto,
                                             @RequestPart(value = "profile", required = false) MultipartFile profile)   {
       ;
        return new ResponseEntity<UserDto>(userService.patchUser(userId, dto,profile), HttpStatus.OK);
    }


    @PostMapping("/reset")
    public void reset(){
        userService.resetUserRepository();
    }

}
