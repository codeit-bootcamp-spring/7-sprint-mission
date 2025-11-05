package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserDeleteRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> signIn(@RequestBody UserCreateRequest request) {
        return new ResponseEntity<>(userService.signIn(request), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> update(@RequestBody UserUpdateRequest request) {
        return new ResponseEntity<>(userService.update(request), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody UserDeleteRequest request) {
        userService.deleteByUserId(request);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAll() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> get(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getByUserId(userId), HttpStatus.OK);
    }


}
