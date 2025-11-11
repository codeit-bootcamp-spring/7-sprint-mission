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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;
//    @RequestMapping(value =  "/register", method = RequestMethod.POST)
//    public ResponseEntity<ApiResponseDto<UserReadResponseDto>> register(@RequestBody UserCreateRequestDto dto){
//
//        ApiResponseDto<UserReadResponseDto> apiResponseDto = ApiResponseDto.success(userService.createUser(dto));
//        return new ResponseEntity<ApiResponseDto<UserReadResponseDto>>(apiResponseDto, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/registerWithProfile", method = RequestMethod.POST)
//    public ResponseEntity<ApiResponseDto<UserReadResponseDto>> registerWithProfile(@RequestBody UserCreateWithProfileRequestDto dto){
//        UserCreateRequestDto userCreateRequestDto = dto.userInfo();
//        ProfileCreateRequestDto profileCreateRequestDto = dto.profileInfo();
//        ApiResponseDto<UserReadResponseDto> apiResponseDto = ApiResponseDto.success(userService.createUser(userCreateRequestDto));
//        return new ResponseEntity<ApiResponseDto<UserReadResponseDto>>(apiResponseDto, HttpStatus.CREATED);
//    }



    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID userId){
        userService.deleteUser(userId);
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> readAll(){
       ;
        return new ResponseEntity<>( userService.advanceFindAllUser(),HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<UserCreateResponseDto> createUser(@RequestPart("userCreateRequest")UserCreateRequestDto dto
    , @RequestPart("profile")MultipartFile profile
                                                                            ) throws IOException {
        return new ResponseEntity<UserCreateResponseDto>(userService.createUser(dto,profile),HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<UserCreateResponseDto> patchUser(@PathVariable UUID userId,
                                                           @RequestPart("userUpdateRequest") UserUpdateRequest dto,
                                                           @RequestPart("profile")MultipartFile profile) throws IOException {
       ;
        return new ResponseEntity<UserCreateResponseDto>(userService.patchUser(userId, dto,profile), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}/userStatus",method = RequestMethod.PATCH)
    public ResponseEntity<UserUserStatusPatchResponseDto> patchUserStatus(@PathVariable UUID userId, @RequestBody UserStatusPatchRequestDto dto){
;
        return new ResponseEntity<UserUserStatusPatchResponseDto>(userStatusService.patchUserStatus(userId, dto), HttpStatus.OK);
    }
//SpringMission5 //////////////////////////////////////////////////////////////////////////////////////
//    @RequestMapping(value = "/updateOnline", method = RequestMethod.GET)
//    public void updateOnline(@RequestParam UUID userId){
//        userService.updateUserOnlineStatus(userId);
//    }
    @RequestMapping(value = "/reset",method = RequestMethod.GET)
    public void reset(){
        userService.resetUserRepository();
    }

    @RequestMapping(value = "/userStatus",method = RequestMethod.GET)
    public ResponseEntity<List<UserStatus>> readAllUserStatus(){
        return new ResponseEntity<>(userStatusService.findAll(),HttpStatus.OK);
    }


//
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public <T>void update(@RequestBody UserUpdateRequestDto<T> dto){
//        userService.updateUser(dto);
//    }
//
//



}
