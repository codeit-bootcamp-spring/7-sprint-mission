package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public UserDto login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
