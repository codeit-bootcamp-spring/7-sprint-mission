package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicUserService;
import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponse;
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
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponse createUser(@ModelAttribute UserCreateRequestDto requestDto) throws IOException {
        UserResponse responseDto = userService.createUser(requestDto);
        return responseDto;
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(@PathVariable UUID id, @ModelAttribute UserUpdateDto updateDto) throws IOException {
        UserResponse userResponse = userService.updateUserInfo(id, updateDto);
        return userResponse;
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
    public List<UserResponse> getAllUSer(){
       return userService.getAllUsers();
    }

}
