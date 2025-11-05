package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.response.auth.AuthLoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
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
    public AuthLoginResponseDto login(@Valid @RequestBody AuthLoginRequestDto authLoginRequestDto) {
        User user = authService.login(authLoginRequestDto);
        return new AuthLoginResponseDto(user.getId(), user.getUsername());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(@RequestParam("userId") UUID userId) {
        authService.logout(userId);
    }
}
