package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicUserService;
import com.sprint.mission.discodeit.application.dto.login.LoginForm;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final BasicUserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public UserResponseDto login(@Valid @RequestBody LoginForm loginForm) {
        return userService.login(loginForm.getLoginId(), loginForm.getPassword());
    }
}
