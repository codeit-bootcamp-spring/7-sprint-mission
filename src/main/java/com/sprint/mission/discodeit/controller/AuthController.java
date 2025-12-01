package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserResponseDto login(@Valid @RequestBody AuthLoginRequestDto authLoginRequestDto) {
        return authService.login(authLoginRequestDto);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(@RequestParam("userId") UUID userId) {
        authService.logout(userId);
    }
}
