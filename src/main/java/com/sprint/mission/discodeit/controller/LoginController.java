package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.LoginRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class LoginController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDto userDto = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
