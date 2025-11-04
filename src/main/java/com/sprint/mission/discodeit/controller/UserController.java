package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestPart("request") CreateUserRequestDto requestDto,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        UserResponseDto newUser;
        try {
            newUser = userService.create(requestDto, file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newUser);
    }

    @RequestMapping(value = "/update/{userId}", method = RequestMethod.POST)
    public ResponseEntity<String> updateUser(@PathVariable UUID userId,
                                             @RequestPart(name = "request", required = false) UpdateUserRequestDto requestDto,
                                             @RequestPart(name = "file", required = false) MultipartFile file) {

        try {
            userService.update(userId, requestDto, file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("User Updated Successfully");
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        try {
            userService.delete(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("User Deleted Successfully");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> searchUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @RequestMapping(value = "/online/{userId}", method = RequestMethod.POST)
    public ResponseEntity<String> onlineUser(@PathVariable UUID userId) {
        try {
            userStatusService.updateByUserId(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("User Online Successfully");
    }

}
