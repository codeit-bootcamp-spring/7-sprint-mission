package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BasicUserService;
import com.sprint.mission.discodeit.service.dto.login.LoginForm;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
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

    private final BasicUserService userService;

    @PostMapping
    public UserDto login(@Valid @RequestBody LoginForm loginForm) {
        return userService.login(loginForm.getUsername(), loginForm.getPassword());
    }
}
