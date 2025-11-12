package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BasicUserService;
import com.sprint.mission.discodeit.service.dto.login.LoginForm;
import com.sprint.mission.discodeit.service.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class LoginController {

    private final BasicUserService userService;

    @PostMapping
    public UserResponse login(@Valid @RequestBody LoginForm loginForm) {
        return userService.login(loginForm.getLoginId(), loginForm.getPassword());
    }
}
