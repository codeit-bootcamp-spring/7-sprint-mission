package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserDeleteRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> signIn(@Valid @RequestBody UserCreateRequest request) {
        return new ResponseEntity<>(userService.signIn(request), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UserUpdateRequest request) {
        return new ResponseEntity<>(userService.update(request), HttpStatus.OK);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@Valid @RequestBody UserDeleteRequest request) {
        userService.delete(request);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAll() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> get(@RequestParam UUID id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }


}
