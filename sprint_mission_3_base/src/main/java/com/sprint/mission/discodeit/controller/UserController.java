package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(method = RequestMethod.POST)
    public UserDto create(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> findAll() {
        return userService.findAll();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDto find(@PathVariable UUID id) {
        return userService.find(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    @RequestMapping(method = RequestMethod.PUT)
    public UserDto update(@RequestBody UserUpdateRequest request) {
        return userService.update(request);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}