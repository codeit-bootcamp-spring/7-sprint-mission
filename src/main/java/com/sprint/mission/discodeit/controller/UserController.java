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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final BasicUserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public UserResponseDto createUser(@ModelAttribute UserCreateRequestDto requestDto) throws IOException {
        UserResponseDto responseDto = userService.createUser(requestDto);
        log.info("회원가입 성공: {}", responseDto.username());
        return responseDto;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public UserResponseDto updateUser(@ModelAttribute UserUpdateDto updateDto) throws IOException {
        UserResponseDto userResponseDto = userService.updateUserInfo(updateDto);
        log.info("회원 수정 성공: {}", userResponseDto.username());
        return userResponseDto;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String deleteUser(@RequestBody UserRequestDto requestDto) {
        userService.delete(requestDto);
        return "삭제완료";
    }

    @RequestMapping("/online")
    public UserResponseDto markOnline(@RequestBody UserRequestDto requestDto) {
        userService.markUserStatus(requestDto.username());
        UserResponseDto responseDto = userService.getUser(requestDto);
        return responseDto;
    }


}
