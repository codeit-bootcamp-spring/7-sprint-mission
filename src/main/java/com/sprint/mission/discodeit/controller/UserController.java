package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserReadResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @RequestMapping(value =  "/register", method = RequestMethod.POST)
    public UserReadResponseDto register(@RequestBody UserCreateRequestDto dto){

       return userService.createUser(dto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public <T>void update(@RequestBody UserUpdateRequestDto<T> dto){
        userService.updateUser(dto);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void delete(@RequestParam UUID userId){
        userService.deleteUser(userId);
    }

    @RequestMapping("/readAll")
    public List<UserReadResponseDto> read(){
        return userService.readAllUser();
    }

    @RequestMapping(value = "/updateOnline", method = RequestMethod.GET)
    public void updateOnline(@RequestParam UUID userId){
        userService.updateUserOnlineStatus(userId);
    }
    @RequestMapping("/reset")
    public void reset(){
        userService.resetUserRepository();
    }
}
