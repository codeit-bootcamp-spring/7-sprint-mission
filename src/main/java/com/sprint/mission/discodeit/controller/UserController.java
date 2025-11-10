package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicUserService;
import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.application.dto.response.UserDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final BasicUserService userService;

    @GetMapping("/findAll")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@ModelAttribute UserCreateRequestDto requestDto) throws IOException {
        UserResponseDto responseDto = userService.createUser(requestDto);
        log.info("회원가입 성공: {}", responseDto.username());
        return responseDto;
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@PathVariable UUID id,@ModelAttribute UserUpdateDto updateDto) throws IOException {
        UserResponseDto userResponseDto = userService.updateUserInfo(id, updateDto);
        log.info("회원 수정 성공: {}", userResponseDto.username());
        return userResponseDto;
    }




    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return "삭제완료";
    }

    @PatchMapping("/{userId}/userStatus")
    public void markOnline(@PathVariable UUID userId,@RequestBody Instant newLastActiveAt) {
        userService.markUserStatus(userId, newLastActiveAt);
    }


    @GetMapping
    public List<UserResponseDto> getAllUSer(){
       return userService.getAllUsers();
    }

}
