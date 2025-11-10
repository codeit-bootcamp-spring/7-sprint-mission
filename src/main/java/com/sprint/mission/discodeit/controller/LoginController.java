package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicUserService;
import com.sprint.mission.discodeit.application.dto.login.LoginForm;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class LoginController {

    private final BasicUserService userService;

    @PostMapping
    public UserResponseDto login(@Valid @RequestBody LoginForm loginForm) {
        return userService.login(loginForm.getLoginId(), loginForm.getPassword());
    }
}
